package com.codeshare.airline.schedule.ingestion.ssim;

import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.context.SsimIngestionContext;
import com.codeshare.airline.schedule.ingestion.dto.common.ssim.SsimCarrierDTO;
import com.codeshare.airline.schedule.ingestion.dto.common.ssim.SsimDataElementDTO;
import com.codeshare.airline.schedule.ingestion.dto.common.ssim.SsimFlightDTO;
import com.codeshare.airline.schedule.ingestion.dto.ssim.SSIMMessageDTO;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationResult;
import com.codeshare.airline.schedule.ingestion.validation.validator.ssim.business.SsimChapter7BusinessValidator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SsimChapter7BusinessValidationTest {

    private final SsimChapter7BusinessValidator validator = new SsimChapter7BusinessValidator();

    @Test
    void chapter7_header_number_of_seasons_must_match_carrier_seasons() {
        ValidationResult result = validator.validate(SsimIngestionContext.builder()
                .messageType(MessageType.SSIM)
                .messageLines(List.of(
                        header("2", "001", "000001"),
                        carrier("U", "QR ", "S26", "01APR26", "30OCT26", "01MAR26", "P", "1200", "000002"),
                        trailer("QR ", "000002", "E", "000003")
                ))
                .build());

        assertThat(result.getMessages())
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("SSIM_CH7_001"));
    }

    @Test
    void chapter7_carrier_and_trailer_must_reconcile() {
        ValidationResult result = validator.validate(SsimIngestionContext.builder()
                .messageType(MessageType.SSIM)
                .messageLines(List.of(
                        header("", "001", "000001"),
                        carrier("U", "QR ", "S26", "01APR26", "30OCT26", "01MAR26", "P", "1200", "000002"),
                        trailerWithRelease("BA ", "30MAR26", "000002", "E", "000003")
                ))
                .build());

        assertThat(result.getMessages())
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("SSIM_CH7_003"))
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("SSIM_CH7_004"));
    }

    @Test
    void chapter7_flight_period_days_and_disclosure_rules_are_validated() {
        SsimCarrierDTO carrier = new SsimCarrierDTO();
        carrier.setAirlineCode("QR");
        carrier.setValidityStartRaw("01APR26");
        carrier.setValidityEndRaw("30APR26");

        SsimFlightDTO flight = new SsimFlightDTO();
        flight.setAirlineCode("BA");
        flight.setFlightNumber("0123");
        flight.setItineraryVariationIdentifier("01");
        flight.setLegSequenceNumber(1);
        flight.setOperatingPeriodStartRaw("15MAR26");
        flight.setOperatingPeriodEndRaw("31MAY26");
        flight.setOperatingDays("1723456");
        flight.setOperatingAirlineDisclosure("X");
        flight.setDepartureStation("DOH");
        flight.setArrivalStation("LHR");

        SSIMMessageDTO parsed = SSIMMessageDTO.builder()
                .carrier(carrier)
                .flights(new ArrayList<>(List.of(flight)))
                .build();

        ValidationResult result = validator.validate(SsimIngestionContext.builder()
                .messageType(MessageType.SSIM)
                .parsedData(parsed)
                .build());

        assertThat(result.getMessages())
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("SSIM_CH7_005"))
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("SSIM_CH7_007"))
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("SSIM_CH7_008"))
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("SSIM_CH7_010"));
    }

    @Test
    void chapter7_segment_indicators_must_match_the_itinerary() {
        SsimFlightDTO leg1 = leg("QR", "0123", "01", 1, "DOH", "LHR");
        SsimFlightDTO leg2 = leg("QR", "0123", "01", 2, "LHR", "JFK");

        SsimDataElementDTO invalid = new SsimDataElementDTO();
        invalid.setDataElementIdentifier("127");
        invalid.setBoardPointIndicator("B");
        invalid.setOffPointIndicator("A");
        invalid.setBoardPoint("LHR");
        invalid.setOffPoint("DOH");
        leg1.getDeis().add(invalid);

        SSIMMessageDTO parsed = SSIMMessageDTO.builder()
                .flights(new ArrayList<>(List.of(leg1, leg2)))
                .build();

        ValidationResult result = validator.validate(SsimIngestionContext.builder()
                .messageType(MessageType.SSIM)
                .parsedData(parsed)
                .build());

        assertThat(result.getMessages())
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("SSIM_CH7_012"));
    }

    private static SsimFlightDTO leg(String airline, String flightNumber, String itineraryVariation, int legSequence, String departure, String arrival) {
        SsimFlightDTO flight = new SsimFlightDTO();
        flight.setAirlineCode(airline);
        flight.setFlightNumber(flightNumber);
        flight.setItineraryVariationIdentifier(itineraryVariation);
        flight.setLegSequenceNumber(legSequence);
        flight.setDepartureStation(departure);
        flight.setArrivalStation(arrival);
        flight.setOperatingPeriodStartRaw("01APR26");
        flight.setOperatingPeriodEndRaw("30APR26");
        flight.setOperatingDays("1234567");
        return flight;
    }

    private static String header(String seasons, String dataSetSerial, String recordSerial) {
        char[] record = blankRecord('1');
        put(record, 1, padRight("AIRLINE STANDARD SCHEDULE DATA SET", 34));
        put(record, 40, seasons);
        put(record, 191, dataSetSerial);
        put(record, 194, recordSerial);
        return new String(record);
    }

    private static String carrier(String timeMode, String airline, String season, String validFrom, String validTo,
                                  String creationDate, String status, String creationTime, String recordSerial) {
        char[] record = blankRecord('2');
        put(record, 1, timeMode);
        put(record, 2, airline);
        put(record, 10, season);
        put(record, 14, validFrom);
        put(record, 21, validTo);
        put(record, 28, creationDate);
        put(record, 64, "29MAR26");
        put(record, 71, status);
        put(record, 190, creationTime);
        put(record, 194, recordSerial);
        return new String(record);
    }

    private static String trailer(String airline, String checkReference, String continuationEndCode, String recordSerial) {
        return trailerWithRelease(airline, "", checkReference, continuationEndCode, recordSerial);
    }

    private static String trailerWithRelease(String airline, String releaseDate, String checkReference, String continuationEndCode, String recordSerial) {
        char[] record = blankRecord('5');
        put(record, 2, airline);
        put(record, 5, releaseDate);
        put(record, 187, checkReference);
        put(record, 193, continuationEndCode);
        put(record, 194, recordSerial);
        return new String(record);
    }

    private static char[] blankRecord(char recordType) {
        char[] record = new char[200];
        java.util.Arrays.fill(record, ' ');
        record[0] = recordType;
        return record;
    }

    private static void put(char[] record, int start, String value) {
        for (int i = 0; i < value.length(); i++) {
            record[start + i] = value.charAt(i);
        }
    }

    private static String padRight(String value, int length) {
        return String.format("%-" + length + "s", value);
    }
}
