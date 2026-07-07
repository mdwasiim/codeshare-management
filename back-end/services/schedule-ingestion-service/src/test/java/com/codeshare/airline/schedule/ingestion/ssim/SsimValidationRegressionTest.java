package com.codeshare.airline.schedule.ingestion.ssim;

import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.context.SsimIngestionContext;
import com.codeshare.airline.schedule.ingestion.domain.enums.ValidationStage;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationResult;
import com.codeshare.airline.schedule.ingestion.validation.validator.ssim.structural.SsimStructuralValidator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SsimValidationRegressionTest {

    private final SsimStructuralValidator structuralValidator = new SsimStructuralValidator();

    @Test
    void manual_record_set_with_header_carrier_flight_segment_and_trailer_is_valid() {
        ValidationResult result = structuralValidator.validate(context(List.of(
                header("001", "000001"),
                carrier("U", "QR ", "01APR26", "30OCT26", "01MAR26", "P", "1200", "000002"),
                flight("QR ", "0123", "01", "01", "J", "01APR26", "30OCT26", "1234567",
                        "DOH", "0800", "0800", "+0300", "LHR", "1400", "1400", "+0000",
                        "320", padRight("Y", 20), padRight("", 20), "01", "000003"),
                dei("QR ", "0123", "01", "01", "J", "N", "N", "050", "DOH", "LHR", "ET", "000004"),
                trailer("QR ", "000004", "E", "000005")
        )));

        assertNoErrors(result);
    }

    @Test
    void manual_type1_record_serial_must_be_000001() {
        ValidationResult result = structuralValidator.validate(context(List.of(
                header("001", "000123"),
                carrier("U", "QR ", "01APR26", "30OCT26", "01MAR26", "P", "1200", "000124"),
                flight("QR ", "0123", "01", "01", "J", "01APR26", "30OCT26", "1234567",
                        "DOH", "0800", "0800", "+0300", "LHR", "1400", "1400", "+0000",
                        "320", padRight("Y", 20), padRight("", 20), "01", "000125"),
                trailer("QR ", "000125", "E", "000126")
        )));

        assertThat(result.getMessages())
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("SSIM_T1_006"));
    }

    @Test
    void manual_type3_requires_prbd_or_aircraft_configuration_version() {
        ValidationResult result = structuralValidator.validate(context(List.of(
                header("001", "000001"),
                carrier("U", "QR ", "01APR26", "30OCT26", "01MAR26", "P", "1200", "000002"),
                flight("QR ", "0123", "01", "01", "J", "01APR26", "30OCT26", "1234567",
                        "DOH", "0800", "0800", "+0300", "LHR", "1400", "1400", "+0000",
                        "320", padRight("", 20), padRight("", 20), "01", "000003"),
                trailer("QR ", "000003", "E", "000004")
        )));

        assertThat(result.getMessages())
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("SSIM_T3_018"));
    }

    @Test
    void manual_type3_date_variation_accepts_previous_day_indicator() {
        ValidationResult result = structuralValidator.validate(context(List.of(
                header("001", "000001"),
                carrier("U", "QR ", "01APR26", "30OCT26", "01MAR26", "P", "1200", "000002"),
                flight("QR ", "0123", "01", "01", "J", "01APR26", "30OCT26", "1234567",
                        "DOH", "0800", "0800", "+0300", "LHR", "1400", "1400", "+0000",
                        "320", padRight("Y", 20), padRight("", 20), "A0", "000003"),
                trailer("QR ", "000003", "E", "000004")
        )));

        assertThat(result.getMessages())
                .noneSatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("SSIM_T3_045"));
    }

    @Test
    void manual_type4_must_immediately_follow_matching_type3() {
        ValidationResult result = structuralValidator.validate(context(List.of(
                header("001", "000001"),
                carrier("U", "QR ", "01APR26", "30OCT26", "01MAR26", "P", "1200", "000002"),
                flight("QR ", "0123", "01", "01", "J", "01APR26", "30OCT26", "1234567",
                        "DOH", "0800", "0800", "+0300", "LHR", "1400", "1400", "+0000",
                        "320", padRight("Y", 20), padRight("", 20), "01", "000003"),
                flight("QR ", "0456", "01", "01", "J", "01APR26", "30OCT26", "1234567",
                        "LHR", "1500", "1500", "+0000", "JFK", "2200", "2200", "-0400",
                        "320", padRight("Y", 20), padRight("", 20), "00", "000004"),
                dei("QR ", "0123", "01", "01", "J", "N", "N", "050", "DOH", "LHR", "ET", "000005"),
                trailer("QR ", "000005", "E", "000006")
        )));

        assertThat(result.getMessages())
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("SSIM_SEQ_009"));
    }

    @Test
    void manual_type5_check_reference_and_record_serial_must_match_previous_sequence() {
        ValidationResult result = structuralValidator.validate(context(List.of(
                header("001", "000001"),
                carrier("U", "QR ", "01APR26", "30OCT26", "01MAR26", "P", "1200", "000002"),
                flight("QR ", "0123", "01", "01", "J", "01APR26", "30OCT26", "1234567",
                        "DOH", "0800", "0800", "+0300", "LHR", "1400", "1400", "+0000",
                        "320", padRight("Y", 20), padRight("", 20), "01", "000003"),
                trailer("QR ", "000111", "E", "000999")
        )));

        assertThat(result.getMessages())
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("SSIM_T5_008"))
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("SSIM_T5_009"));
    }

    @Test
    void extra_malformed_zero_padding_record_is_rejected() {
        ValidationResult result = structuralValidator.validate(context(List.of(
                header("001", "000001"),
                "0".repeat(199) + "1",
                carrier("U", "QR ", "01APR26", "30OCT26", "01MAR26", "P", "1200", "000002"),
                flight("QR ", "0123", "01", "01", "J", "01APR26", "30OCT26", "1234567",
                        "DOH", "0800", "0800", "+0300", "LHR", "1400", "1400", "+0000",
                        "320", padRight("Y", 20), padRight("", 20), "01", "000003"),
                trailer("QR ", "000003", "E", "000004")
        )));

        assertThat(result.getMessages())
                .anySatisfy(message -> assertThat(message.getRuleCode()).isEqualTo("SSIM_PAD_001"));
    }

    @Test
    void extra_empty_message_is_rejected() {
        ValidationResult result = structuralValidator.validate(SsimIngestionContext.builder()
                .messageType(MessageType.SSIM)
                .messageLines(List.of())
                .build());

        assertThat(result.getMessages())
                .anySatisfy(message -> {
                    assertThat(message.getRuleCode()).isEqualTo("SSIM_001");
                    assertThat(message.getStage()).isEqualTo(ValidationStage.STRUCTURAL);
                });
    }

    private SsimIngestionContext context(List<String> lines) {
        return SsimIngestionContext.builder()
                .messageType(MessageType.SSIM)
                .messageLines(lines)
                .build();
    }

    private void assertNoErrors(ValidationResult result) {
        assertThat(result.getMessages())
                .filteredOn(message -> message.getSeverity().name().equals("ERROR"))
                .isEmpty();
    }

    private static String header(String dataSetSerial, String recordSerial) {
        char[] record = blankRecord('1');
        put(record, 1, padRight("AIRLINE STANDARD SCHEDULE DATA SET", 34));
        put(record, 191, dataSetSerial);
        put(record, 194, recordSerial);
        return new String(record);
    }

    private static String carrier(String timeMode, String airline, String validFrom, String validTo,
                                  String creationDate, String status, String creationTime, String recordSerial) {
        char[] record = blankRecord('2');
        put(record, 1, timeMode);
        put(record, 2, airline);
        put(record, 14, validFrom);
        put(record, 21, validTo);
        put(record, 28, creationDate);
        put(record, 71, status);
        put(record, 190, creationTime);
        put(record, 194, recordSerial);
        return new String(record);
    }

    private static String flight(String airline, String flightNumber, String itineraryVariation, String legSequence,
                                 String serviceType, String periodFrom, String periodTo, String days,
                                 String depStation, String passengerStd, String aircraftStd, String depUtc,
                                 String arrStation, String aircraftSta, String passengerSta, String arrUtc,
                                 String aircraftType, String prbd, String acv, String dateVariation,
                                 String recordSerial) {
        char[] record = blankRecord('3');
        put(record, 2, airline);
        put(record, 5, flightNumber);
        put(record, 9, itineraryVariation);
        put(record, 11, legSequence);
        put(record, 13, serviceType);
        put(record, 14, periodFrom);
        put(record, 21, periodTo);
        put(record, 28, days);
        put(record, 36, depStation);
        put(record, 39, passengerStd);
        put(record, 43, aircraftStd);
        put(record, 47, depUtc);
        put(record, 54, arrStation);
        put(record, 57, aircraftSta);
        put(record, 61, passengerSta);
        put(record, 65, arrUtc);
        put(record, 72, aircraftType);
        put(record, 75, prbd);
        put(record, 172, acv);
        put(record, 192, dateVariation);
        put(record, 194, recordSerial);
        return new String(record);
    }

    private static String dei(String airline, String flightNumber, String itineraryVariation, String legSequence,
                              String serviceType, String boardIndicator, String offIndicator, String deiCode,
                              String boardPoint, String offPoint, String data, String recordSerial) {
        char[] record = blankRecord('4');
        put(record, 2, airline);
        put(record, 5, flightNumber);
        put(record, 9, itineraryVariation);
        put(record, 11, legSequence);
        put(record, 13, serviceType);
        put(record, 28, boardIndicator);
        put(record, 29, offIndicator);
        put(record, 30, deiCode);
        put(record, 33, boardPoint);
        put(record, 36, offPoint);
        put(record, 39, data);
        put(record, 194, recordSerial);
        return new String(record);
    }

    private static String trailer(String airline, String checkReference, String continuationEndCode, String recordSerial) {
        char[] record = blankRecord('5');
        put(record, 2, airline);
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
