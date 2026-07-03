package com.codeshare.airline.schedule.ingestion.orchestration.parsers;

import com.codeshare.airline.schedule.ingestion.config.ScheduleIngestionProperties;
import com.codeshare.airline.schedule.ingestion.domain.context.ScheduleGroupedMessage;
import com.codeshare.airline.schedule.ingestion.domain.enums.RecordType;
import com.codeshare.airline.schedule.ingestion.domain.enums.SsimValidationMode;
import com.codeshare.airline.schedule.ingestion.domain.enums.TimeMode;
import com.codeshare.airline.schedule.ingestion.dto.common.ssim.SsimCarrierDTO;
import com.codeshare.airline.schedule.ingestion.dto.common.ssim.SsimDataElementDTO;
import com.codeshare.airline.schedule.ingestion.dto.common.ssim.SsimFlightDTO;
import com.codeshare.airline.schedule.ingestion.dto.common.ssim.SsimHeaderDTO;
import com.codeshare.airline.schedule.ingestion.dto.common.ssim.SsimTrailerDTO;
import com.codeshare.airline.schedule.ingestion.dto.ssim.SSIMMessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.codeshare.airline.schedule.ingestion.domain.enums.RecordType.CARRIER;
import static com.codeshare.airline.schedule.ingestion.domain.enums.RecordType.DEI;
import static com.codeshare.airline.schedule.ingestion.domain.enums.RecordType.HEADER;
import static com.codeshare.airline.schedule.ingestion.domain.enums.RecordType.LEG;

@Slf4j
@Component
public class SsimMessageParser implements ScheduleParser<SSIMMessageDTO> {

    private static final int SSIM_RECORD_LENGTH = 200;

    private final SsimValidationMode validationMode;

    public SsimMessageParser() {
        this.validationMode = SsimValidationMode.RELAXED;
    }

    @Autowired
    public SsimMessageParser(ScheduleIngestionProperties properties) {
        this.validationMode = properties.getSsim().getValidationMode() == null
                ? SsimValidationMode.RELAXED
                : properties.getSsim().getValidationMode();
    }

    @Override
    public SSIMMessageDTO parseMessage(ScheduleGroupedMessage scheduleGroupedMessage) {
        if (scheduleGroupedMessage.getOriginalLines() == null || scheduleGroupedMessage.getOriginalLines().isEmpty()) {
            throw new IllegalArgumentException("SSIM input is empty");
        }

        SSIMMessageDTO message = new SSIMMessageDTO();

        for (String line : scheduleGroupedMessage.getOriginalLines()) {
            if (line == null || line.isBlank()) {
                continue;
            }

            validateRecordLength(line);

            char recordChar = line.charAt(0);
            if (recordChar == '0') {
                continue;
            }

            RecordType recordType = RecordType.fromCode(Character.getNumericValue(recordChar));

            switch (recordType) {
                case HEADER -> message.setHeader(processRecord1(line));
                case CARRIER -> message.setCarrier(processRecord2(line));
                case LEG -> {
                    SsimFlightDTO flightDTO = processRecord3(line);
                    message.addFlight(flightDTO);
                    if (message.getFlightNumber() == null) {
                        message.setFlightNumber(flightDTO.getFlightNumber());
                        message.setAirlineCode(flightDTO.getAirlineCode());
                    }
                }
                case DEI -> attachDei(line, message.getFlights());
                case TRAILER -> message.setTrailer(processRecord5(line));
                default -> log.warn("Unknown SSIM record: {}", line);
            }
        }

        if (message.getFlights().isEmpty()) {
            log.warn("No SSIM flight records parsed");
        }

        return message;
    }

    private void attachDei(String line, List<SsimFlightDTO> legs) {
        if (legs.isEmpty()) {
            handleInvalidDei("DEI without previous leg: " + line);
            return;
        }

        SsimDataElementDTO dei = processRecord4(line);
        findMatchingLeg(legs, dei).getDeis().add(dei);
    }

    private SsimFlightDTO findMatchingLeg(List<SsimFlightDTO> legs, SsimDataElementDTO dei) {
        for (int i = legs.size() - 1; i >= 0; i--) {
            SsimFlightDTO leg = legs.get(i);
            if (same(leg.getOperationalSuffix(), dei.getOperationalSuffix())
                    && same(leg.getAirlineCode(), dei.getAirlineCode())
                    && same(leg.getFlightNumber(), dei.getFlightNumber())
                    && same(leg.getItineraryVariationIdentifier(), dei.getItineraryVariationIdentifier())
                    && sameLegSequence(leg.getLegSequenceNumber(), dei.getLegSequenceNumber())
                    && same(leg.getServiceType(), dei.getServiceType())) {
                return leg;
            }
        }

        if (isStrict()) {
            throw new IllegalArgumentException("DEI did not match a previous leg by full identity: " + dei);
        }
        log.warn("DEI did not match a previous leg by full identity; attaching to latest leg: {}", dei);
        return legs.getLast();
    }

    private boolean same(String left, String right) {
        return normalize(left).equals(normalize(right));
    }

    private boolean sameLegSequence(Integer left, String right) {
        if (left == null || right == null || right.isBlank()) {
            return false;
        }
        try {
            return left == Integer.parseInt(right.trim());
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    private SsimHeaderDTO processRecord1(String line) {
        if (line.charAt(0) != '1') {
            throw new IllegalArgumentException("Invalid Record Type. Expected '1', got: " + line.charAt(0));
        }

        SsimHeaderDTO header = new SsimHeaderDTO();
        header.setRecordType(HEADER);
        header.setTitleOfContents(line.substring(1, 35));
        header.setSpare36To40(line.substring(35, 40));
        header.setSpare42To191(line.substring(41, 191));

        String seasonsStr = line.substring(40, 41);
        if (seasonsStr.trim().isEmpty()) {
            header.setNumberOfSeasons(null);
        } else if (seasonsStr.matches("\\d")) {
            header.setNumberOfSeasons(Integer.parseInt(seasonsStr));
        } else {
            throw new IllegalArgumentException("Invalid Number of Seasons at pos 41: [" + seasonsStr + "]");
        }

        String datasetSerial = line.substring(191, 194);
        if (!datasetSerial.matches("\\d{3}")) {
            throw new IllegalArgumentException("Invalid Dataset Serial Number: " + datasetSerial);
        }
        header.setDatasetSerialNumber(datasetSerial);

        String recordSerial = line.substring(194, 200);
        if (!recordSerial.matches("\\d{6}")) {
            throw new IllegalArgumentException("Invalid Record Serial Number: " + recordSerial);
        }
        header.setRecordSerialNumber(recordSerial);

        return header;
    }

    private SsimCarrierDTO processRecord2(String line) {
        SsimCarrierDTO carrier = new SsimCarrierDTO();
        carrier.setRecordType(CARRIER);

        String timeModeStr = line.substring(1, 2);
        TimeMode timeMode = null;
        if ("L".equals(timeModeStr)) {
            timeMode = TimeMode.LT;
        } else if ("U".equals(timeModeStr)) {
            timeMode = TimeMode.UTC;
        } else if (isStrict()) {
            throw new IllegalArgumentException("Invalid SSIM Type 2 time mode: [" + timeModeStr + "]");
        }
        carrier.setTimeMode(timeMode);

        carrier.setAirlineCode(line.substring(2, 5));
        carrier.setSpare6To10(line.substring(5, 10));
        carrier.setSeason(line.substring(10, 13));
        carrier.setSpare14(line.substring(13, 14));
        carrier.setValidityStartRaw(line.substring(14, 21));
        carrier.setValidityEndRaw(line.substring(21, 28));
        carrier.setCreationDateRaw(line.substring(28, 35));
        carrier.setTitleOfData(line.substring(35, 64));
        carrier.setReleaseDateRaw(line.substring(64, 71));
        carrier.setScheduleStatus(line.substring(71, 72));
        carrier.setCreatorReference(line.substring(72, 107));
        carrier.setDuplicateDesignatorMarker(line.substring(107, 108));
        carrier.setGeneralInformation(line.substring(108, 169));
        carrier.setInflightServiceInfo(line.substring(169, 188));
        carrier.setElectronicTicketingInfo(line.substring(188, 190));
        carrier.setCreationTimeRaw(line.substring(190, 194));
        carrier.setRecordSerialNumber(line.substring(194, 200));

        return carrier;
    }

    private SsimFlightDTO processRecord3(String line) {
        SsimFlightDTO leg = new SsimFlightDTO();
        leg.setRecordType(LEG);

        leg.setOperationalSuffix(line.substring(1, 2));
        leg.setAirlineCode(line.substring(2, 5));
        leg.setFlightNumber(line.substring(5, 9));
        leg.setItineraryVariationIdentifier(line.substring(9, 11));
        leg.setLegSequenceNumber(parseLegSequenceNumber(line.substring(11, 13)));
        leg.setServiceType(line.substring(13, 14));
        leg.setOperatingPeriodStartRaw(line.substring(14, 21));
        leg.setOperatingPeriodEndRaw(line.substring(21, 28));
        leg.setOperatingDays(line.substring(28, 35));
        leg.setFrequencyRate(line.substring(35, 36));
        leg.setDepartureStation(line.substring(36, 39));
        leg.setPassengerStd(line.substring(39, 43));
        leg.setAircraftStd(line.substring(43, 47));
        leg.setDepartureUtcVariation(line.substring(47, 52));
        leg.setDepartureTerminal(line.substring(52, 54));
        leg.setArrivalStation(line.substring(54, 57));
        leg.setAircraftSta(line.substring(57, 61));
        leg.setPassengerSta(line.substring(61, 65));
        leg.setArrivalUtcVariation(line.substring(65, 70));
        leg.setArrivalTerminal(line.substring(70, 72));
        leg.setAircraftType(line.substring(72, 75));
        leg.setPassengerReservationBookingDesignator(line.substring(75, 95));
        leg.setPassengerReservationBookingModifier(line.substring(95, 100));
        leg.setMealServiceNote(line.substring(100, 110));
        leg.setJointOperationAirlineDesignators(line.substring(110, 119));
        leg.setMinimumConnectingTimeStatus(line.substring(119, 121));
        leg.setSecureFlightIndicator(line.substring(121, 122));
        leg.setSpare123To127(line.substring(122, 127));
        leg.setItineraryVariationOverflow(line.substring(127, 128));
        leg.setAircraftOwner(line.substring(128, 131));
        leg.setCockpitCrewEmployer(line.substring(131, 134));
        leg.setCabinCrewEmployer(line.substring(134, 137));
        leg.setOnwardAirlineDesignator(line.substring(137, 140));
        leg.setOnwardFlightNumber(line.substring(140, 144));
        leg.setAircraftRotationLayover(line.substring(144, 145));
        leg.setOnwardOperationalSuffix(line.substring(145, 146));
        leg.setSpare147(line.substring(146, 147));
        leg.setFlightTransitLayover(line.substring(147, 148));
        leg.setOperatingAirlineDisclosure(line.substring(148, 149));
        leg.setTrafficRestrictionCode(line.substring(149, 160));
        leg.setTrafficRestrictionOverflow(line.substring(160, 161));
        leg.setSpare162To172(line.substring(161, 172));
        leg.setAircraftConfigurationVersion(line.substring(172, 192));
        leg.setDateVariation(line.substring(192, 194));
        leg.setRecordSerialNumber(line.substring(194, 200));

        return leg;
    }

    private SsimDataElementDTO processRecord4(String line) {
        SsimDataElementDTO dei = new SsimDataElementDTO();
        dei.setRecordType(DEI);

        dei.setOperationalSuffix(line.substring(1, 2));
        dei.setAirlineCode(line.substring(2, 5));
        dei.setFlightNumber(line.substring(5, 9));
        dei.setItineraryVariationIdentifier(line.substring(9, 11));
        dei.setLegSequenceNumber(line.substring(11, 13));
        dei.setServiceType(line.substring(13, 14));
        dei.setSpare15To27(line.substring(14, 27));
        dei.setItineraryVariationOverflow(line.substring(27, 28));
        dei.setBoardPointIndicator(line.substring(28, 29));
        dei.setOffPointIndicator(line.substring(29, 30));
        dei.setDataElementIdentifier(line.substring(30, 33));
        dei.setBoardPoint(line.substring(33, 36));
        dei.setOffPoint(line.substring(36, 39));
        dei.setDeiData(line.substring(39, 194));
        dei.setRecordSerialNumber(line.substring(194, 200));

        return dei;
    }

    private SsimTrailerDTO processRecord5(String line) {
        SsimTrailerDTO trailer = new SsimTrailerDTO();
        trailer.setRecordType(RecordType.fromCode(Character.getNumericValue(line.charAt(0))));
        trailer.setSpareByte2(line.substring(1, 2));
        trailer.setAirlineDesignator(line.substring(2, 5));
        trailer.setReleaseDateRaw(line.substring(5, 12));
        trailer.setSpare13To187(line.substring(12, 187));
        trailer.setSerialCheckReference(line.substring(187, 193));
        trailer.setContinuationEndCode(line.substring(193, 194));
        trailer.setRecordSerialNumber(line.substring(194, 200));

        return trailer;
    }

    private Integer parseLegSequenceNumber(String value) {
        if (value == null || !value.matches("\\d{2}")) {
            throw new IllegalArgumentException("Invalid SSIM Type 3 leg sequence number: [" + value + "]");
        }
        return Integer.valueOf(value);
    }

    private void handleInvalidDei(String message) {
        if (isStrict()) {
            throw new IllegalArgumentException(message);
        }
        log.warn(message);
    }

    private boolean isStrict() {
        return validationMode == SsimValidationMode.STRICT;
    }

    private void validateRecordLength(String line) {
        if (line.length() != SSIM_RECORD_LENGTH) {
            throw new IllegalArgumentException(
                    "Invalid SSIM record length. Expected " + SSIM_RECORD_LENGTH + ", got " + line.length()
            );
        }
    }

    @Override
    public ScheduleGroupedMessage groupMessage(List<String> lines) {
        return new ScheduleGroupedMessage(null, null, lines);
    }
}
