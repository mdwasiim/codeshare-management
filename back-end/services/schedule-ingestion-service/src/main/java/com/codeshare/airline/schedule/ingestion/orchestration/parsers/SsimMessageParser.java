package com.codeshare.airline.schedule.ingestion.orchestration.parsers;

import com.codeshare.airline.schedule.ingestion.config.ScheduleIngestionProperties;
import com.codeshare.airline.schedule.ingestion.domain.context.ScheduleGroupedMessage;
import com.codeshare.airline.schedule.ingestion.domain.enums.RecordType;
import com.codeshare.airline.schedule.ingestion.domain.enums.SsimValidationMode;
import com.codeshare.airline.schedule.ingestion.domain.enums.TimeMode;
import com.codeshare.airline.schedule.ingestion.dto.ssim.record.SsimCarrierDTO;
import com.codeshare.airline.schedule.ingestion.dto.ssim.record.SsimDataElementDTO;
import com.codeshare.airline.schedule.ingestion.dto.ssim.record.SsimFlightDTO;
import com.codeshare.airline.schedule.ingestion.dto.ssim.record.SsimHeaderDTO;
import com.codeshare.airline.schedule.ingestion.dto.ssim.record.SsimTrailerDTO;
import com.codeshare.airline.schedule.ingestion.dto.ssim.SSIMMessageDTO;
import com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.codeshare.airline.schedule.ingestion.domain.enums.RecordType.CARRIER;
import static com.codeshare.airline.schedule.ingestion.domain.enums.RecordType.DEI;
import static com.codeshare.airline.schedule.ingestion.domain.enums.RecordType.HEADER;
import static com.codeshare.airline.schedule.ingestion.domain.enums.RecordType.LEG;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T1_DATASET_SERIAL;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T1_NUMBER_OF_SEASONS;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T1_RECORD_SERIAL;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T1_SPARE_36_40;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T1_SPARE_42_191;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T1_TITLE;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T2_AIRLINE;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T2_CREATION_DATE;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T2_CREATION_TIME;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T2_CREATOR_REFERENCE;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T2_DUPLICATE_MARKER;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T2_ET_INFO;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T2_GENERAL_INFO;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T2_INFLIGHT_SERVICE;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T2_RECORD_SERIAL;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T2_RELEASE_DATE;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T2_SEASON;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T2_SPARE_14;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T2_SPARE_6_10;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T2_STATUS;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T2_TIME_MODE;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T2_TITLE;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T2_VALIDITY_END;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T2_VALIDITY_START;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_ACV;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_AIRCRAFT_OWNER;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_AIRCRAFT_ROTATION_LAYOVER;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_AIRCRAFT_STA;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_AIRCRAFT_STD;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_AIRCRAFT_TYPE;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_AIRLINE;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_ARRIVAL_STATION;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_ARRIVAL_TERMINAL;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_ARRIVAL_UTC_VARIATION;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_CABIN_EMPLOYER;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_COCKPIT_EMPLOYER;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_DATE_VARIATION;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_DEPARTURE_STATION;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_DEPARTURE_TERMINAL;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_DEPARTURE_UTC_VARIATION;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_DISCLOSURE;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_FLIGHT_NUMBER;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_FLIGHT_TRANSIT_LAYOVER;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_FREQUENCY;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_IVI;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_IVI_OVERFLOW;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_JOINT_OPERATION;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_LEG_SEQUENCE;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_MCT_STATUS;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_MEAL_SERVICE;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_ONWARD_AIRLINE;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_ONWARD_FLIGHT_NUMBER;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_ONWARD_OPERATIONAL_SUFFIX;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_OPERATIONAL_SUFFIX;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_OPERATING_DAYS;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_PASSENGER_STA;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_PASSENGER_STD;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_PERIOD_END;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_PERIOD_START;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_PRBD;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_PRBM;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_RECORD_SERIAL;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_SECURE_FLIGHT;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_SERVICE_TYPE;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_SPARE_123_127;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_SPARE_147;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_SPARE_162_172;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_TRAFFIC_RESTRICTION;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T3_TRAFFIC_RESTRICTION_OVERFLOW;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T4_AIRLINE;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T4_BOARD_INDICATOR;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T4_BOARD_POINT;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T4_DATA;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T4_DEI;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T4_FLIGHT_NUMBER;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T4_IVI;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T4_IVI_OVERFLOW;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T4_LEG_SEQUENCE;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T4_OFF_INDICATOR;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T4_OFF_POINT;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T4_OPERATIONAL_SUFFIX;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T4_RECORD_SERIAL;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T4_SERVICE_TYPE;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T4_SPARE_15_27;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T5_AIRLINE;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T5_CONTINUATION_END;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T5_RECORD_SERIAL;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T5_RELEASE_DATE;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T5_SERIAL_CHECK_REFERENCE;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T5_SPARE_13_187;
import static com.codeshare.airline.schedule.ingestion.orchestration.parsers.ssim.SsimRecordLayout.T5_SPARE_2;

@Slf4j
@Component
public class SsimMessageParser implements ScheduleParser<SSIMMessageDTO> {

    private final SsimValidationMode validationMode;

    public SsimMessageParser(ScheduleIngestionProperties properties) {
        this.validationMode = properties.getSsim().getValidationMode() == null
                ? SsimValidationMode.RELAXED
                : properties.getSsim().getValidationMode();
    }

    @Override
    public SSIMMessageDTO parseMessage(ScheduleGroupedMessage groupedMessage) {
        if (groupedMessage.getOriginalLines() == null || groupedMessage.getOriginalLines().isEmpty()) {
            throw new IllegalArgumentException("SSIM input is empty");
        }

        SSIMMessageDTO message = new SSIMMessageDTO();
        for (String line : groupedMessage.getOriginalLines()) {
            if (line == null || line.isBlank()) {
                continue;
            }

            validateRecordLength(line);

            char recordCode = line.charAt(0);
            if (recordCode == '0') {
                continue;
            }

            RecordType recordType = RecordType.fromCode(Character.getNumericValue(recordCode));
            switch (recordType) {
                case HEADER -> message.setHeader(processHeader(line));
                case CARRIER -> message.setCarrier(processCarrier(line));
                case LEG -> appendFlight(message, processFlight(line));
                case DEI -> attachDei(line, message.getFlights());
                case TRAILER -> message.setTrailer(processTrailer(line));
                default -> log.warn("Unknown SSIM record: {}", line);
            }
        }

        if (message.getFlights().isEmpty()) {
            log.warn("No SSIM flight records parsed");
        }

        return message;
    }

    @Override
    public ScheduleGroupedMessage groupMessage(List<String> lines) {
        return new ScheduleGroupedMessage(null, null, lines);
    }

    private void appendFlight(SSIMMessageDTO message, SsimFlightDTO flight) {
        message.addFlight(flight);
        if (message.getFlightNumber() == null) {
            message.setFlightNumber(flight.getFlightNumber());
            message.setAirlineCode(flight.getAirlineCode());
        }
    }

    private void attachDei(String line, List<SsimFlightDTO> legs) {
        if (legs.isEmpty()) {
            handleInvalidDei("DEI without previous leg: " + line);
            return;
        }

        SsimDataElementDTO dei = processDei(line);
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

    private SsimHeaderDTO processHeader(String line) {
        if (line.charAt(0) != '1') {
            throw new IllegalArgumentException("Invalid Record Type. Expected '1', got: " + line.charAt(0));
        }

        SsimHeaderDTO header = new SsimHeaderDTO();
        header.setRecordType(HEADER);
        header.setTitleOfContents(T1_TITLE.read(line));
        header.setSpare36To40(T1_SPARE_36_40.read(line));
        header.setSpare42To191(T1_SPARE_42_191.read(line));

        String seasonCount = T1_NUMBER_OF_SEASONS.read(line);
        if (seasonCount.trim().isEmpty()) {
            header.setNumberOfSeasons(null);
        } else if (seasonCount.matches("\\d")) {
            header.setNumberOfSeasons(Integer.parseInt(seasonCount));
        } else {
            throw new IllegalArgumentException("Invalid Number of Seasons at pos 41: [" + seasonCount + "]");
        }

        String dataSetSerial = T1_DATASET_SERIAL.read(line);
        if (!dataSetSerial.matches("\\d{3}")) {
            throw new IllegalArgumentException("Invalid Dataset Serial Number: " + dataSetSerial);
        }
        header.setDatasetSerialNumber(dataSetSerial);

        String recordSerial = T1_RECORD_SERIAL.read(line);
        if (!recordSerial.matches("\\d{6}")) {
            throw new IllegalArgumentException("Invalid Record Serial Number: " + recordSerial);
        }
        header.setRecordSerialNumber(recordSerial);
        return header;
    }

    private SsimCarrierDTO processCarrier(String line) {
        SsimCarrierDTO carrier = new SsimCarrierDTO();
        carrier.setRecordType(CARRIER);
        carrier.setTimeMode(parseTimeMode(T2_TIME_MODE.read(line)));
        carrier.setAirlineCode(T2_AIRLINE.read(line));
        carrier.setSpare6To10(T2_SPARE_6_10.read(line));
        carrier.setSeason(T2_SEASON.read(line));
        carrier.setSpare14(T2_SPARE_14.read(line));
        carrier.setValidityStartRaw(T2_VALIDITY_START.read(line));
        carrier.setValidityEndRaw(T2_VALIDITY_END.read(line));
        carrier.setCreationDateRaw(T2_CREATION_DATE.read(line));
        carrier.setTitleOfData(T2_TITLE.read(line));
        carrier.setReleaseDateRaw(T2_RELEASE_DATE.read(line));
        carrier.setScheduleStatus(T2_STATUS.read(line));
        carrier.setCreatorReference(T2_CREATOR_REFERENCE.read(line));
        carrier.setDuplicateDesignatorMarker(T2_DUPLICATE_MARKER.read(line));
        carrier.setGeneralInformation(T2_GENERAL_INFO.read(line));
        carrier.setInflightServiceInfo(T2_INFLIGHT_SERVICE.read(line));
        carrier.setElectronicTicketingInfo(T2_ET_INFO.read(line));
        carrier.setCreationTimeRaw(T2_CREATION_TIME.read(line));
        carrier.setRecordSerialNumber(T2_RECORD_SERIAL.read(line));
        return carrier;
    }

    private SsimFlightDTO processFlight(String line) {
        SsimFlightDTO flight = new SsimFlightDTO();
        flight.setRecordType(LEG);
        flight.setOperationalSuffix(T3_OPERATIONAL_SUFFIX.read(line));
        flight.setAirlineCode(T3_AIRLINE.read(line));
        flight.setFlightNumber(T3_FLIGHT_NUMBER.read(line));
        flight.setItineraryVariationIdentifier(T3_IVI.read(line));
        flight.setLegSequenceNumber(parseLegSequenceNumber(T3_LEG_SEQUENCE.read(line)));
        flight.setServiceType(T3_SERVICE_TYPE.read(line));
        flight.setOperatingPeriodStartRaw(T3_PERIOD_START.read(line));
        flight.setOperatingPeriodEndRaw(T3_PERIOD_END.read(line));
        flight.setOperatingDays(T3_OPERATING_DAYS.read(line));
        flight.setFrequencyRate(T3_FREQUENCY.read(line));
        flight.setDepartureStation(T3_DEPARTURE_STATION.read(line));
        flight.setPassengerStd(T3_PASSENGER_STD.read(line));
        flight.setAircraftStd(T3_AIRCRAFT_STD.read(line));
        flight.setDepartureUtcVariation(T3_DEPARTURE_UTC_VARIATION.read(line));
        flight.setDepartureTerminal(T3_DEPARTURE_TERMINAL.read(line));
        flight.setArrivalStation(T3_ARRIVAL_STATION.read(line));
        flight.setAircraftSta(T3_AIRCRAFT_STA.read(line));
        flight.setPassengerSta(T3_PASSENGER_STA.read(line));
        flight.setArrivalUtcVariation(T3_ARRIVAL_UTC_VARIATION.read(line));
        flight.setArrivalTerminal(T3_ARRIVAL_TERMINAL.read(line));
        flight.setAircraftType(T3_AIRCRAFT_TYPE.read(line));
        flight.setPassengerReservationBookingDesignator(T3_PRBD.read(line));
        flight.setPassengerReservationBookingModifier(T3_PRBM.read(line));
        flight.setMealServiceNote(T3_MEAL_SERVICE.read(line));
        flight.setJointOperationAirlineDesignators(T3_JOINT_OPERATION.read(line));
        flight.setMinimumConnectingTimeStatus(T3_MCT_STATUS.read(line));
        flight.setSecureFlightIndicator(T3_SECURE_FLIGHT.read(line));
        flight.setSpare123To127(T3_SPARE_123_127.read(line));
        flight.setItineraryVariationOverflow(T3_IVI_OVERFLOW.read(line));
        flight.setAircraftOwner(T3_AIRCRAFT_OWNER.read(line));
        flight.setCockpitCrewEmployer(T3_COCKPIT_EMPLOYER.read(line));
        flight.setCabinCrewEmployer(T3_CABIN_EMPLOYER.read(line));
        flight.setOnwardAirlineDesignator(T3_ONWARD_AIRLINE.read(line));
        flight.setOnwardFlightNumber(T3_ONWARD_FLIGHT_NUMBER.read(line));
        flight.setAircraftRotationLayover(T3_AIRCRAFT_ROTATION_LAYOVER.read(line));
        flight.setOnwardOperationalSuffix(T3_ONWARD_OPERATIONAL_SUFFIX.read(line));
        flight.setSpare147(T3_SPARE_147.read(line));
        flight.setFlightTransitLayover(T3_FLIGHT_TRANSIT_LAYOVER.read(line));
        flight.setOperatingAirlineDisclosure(T3_DISCLOSURE.read(line));
        flight.setTrafficRestrictionCode(T3_TRAFFIC_RESTRICTION.read(line));
        flight.setTrafficRestrictionOverflow(T3_TRAFFIC_RESTRICTION_OVERFLOW.read(line));
        flight.setSpare162To172(T3_SPARE_162_172.read(line));
        flight.setAircraftConfigurationVersion(T3_ACV.read(line));
        flight.setDateVariation(T3_DATE_VARIATION.read(line));
        flight.setRecordSerialNumber(T3_RECORD_SERIAL.read(line));
        return flight;
    }

    private SsimDataElementDTO processDei(String line) {
        SsimDataElementDTO dei = new SsimDataElementDTO();
        dei.setRecordType(DEI);
        dei.setOperationalSuffix(T4_OPERATIONAL_SUFFIX.read(line));
        dei.setAirlineCode(T4_AIRLINE.read(line));
        dei.setFlightNumber(T4_FLIGHT_NUMBER.read(line));
        dei.setItineraryVariationIdentifier(T4_IVI.read(line));
        dei.setLegSequenceNumber(T4_LEG_SEQUENCE.read(line));
        dei.setServiceType(T4_SERVICE_TYPE.read(line));
        dei.setSpare15To27(T4_SPARE_15_27.read(line));
        dei.setItineraryVariationOverflow(T4_IVI_OVERFLOW.read(line));
        dei.setBoardPointIndicator(T4_BOARD_INDICATOR.read(line));
        dei.setOffPointIndicator(T4_OFF_INDICATOR.read(line));
        dei.setDataElementIdentifier(T4_DEI.read(line));
        dei.setBoardPoint(T4_BOARD_POINT.read(line));
        dei.setOffPoint(T4_OFF_POINT.read(line));
        dei.setDeiData(T4_DATA.read(line));
        dei.setRecordSerialNumber(T4_RECORD_SERIAL.read(line));
        return dei;
    }

    private SsimTrailerDTO processTrailer(String line) {
        SsimTrailerDTO trailer = new SsimTrailerDTO();
        trailer.setRecordType(RecordType.fromCode(Character.getNumericValue(line.charAt(0))));
        trailer.setSpareByte2(T5_SPARE_2.read(line));
        trailer.setAirlineDesignator(T5_AIRLINE.read(line));
        trailer.setReleaseDateRaw(T5_RELEASE_DATE.read(line));
        trailer.setSpare13To187(T5_SPARE_13_187.read(line));
        trailer.setSerialCheckReference(T5_SERIAL_CHECK_REFERENCE.read(line));
        trailer.setContinuationEndCode(T5_CONTINUATION_END.read(line));
        trailer.setRecordSerialNumber(T5_RECORD_SERIAL.read(line));
        return trailer;
    }

    private TimeMode parseTimeMode(String rawValue) {
        return switch (rawValue) {
            case "L" -> TimeMode.LT;
            case "U" -> TimeMode.UTC;
            default -> {
                if (isStrict()) {
                    throw new IllegalArgumentException("Invalid SSIM Type 2 time mode: [" + rawValue + "]");
                }
                yield null;
            }
        };
    }

    private Integer parseLegSequenceNumber(String value) {
        if (value == null || !value.matches("\\d{2}")) {
            throw new IllegalArgumentException("Invalid SSIM Type 3 leg sequence number: [" + value + "]");
        }
        return Integer.valueOf(value);
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
        if (line.length() != SsimRecordLayout.RECORD_LENGTH) {
            throw new IllegalArgumentException(
                    "Invalid SSIM record length. Expected " + SsimRecordLayout.RECORD_LENGTH + ", got " + line.length()
            );
        }
    }
}

