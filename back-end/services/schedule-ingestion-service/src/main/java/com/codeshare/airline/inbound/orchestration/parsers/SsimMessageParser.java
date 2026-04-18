package com.codeshare.airline.inbound.orchestration.parsers;

import com.codeshare.airline.inbound.domain.context.ScheduleGroupedMessage;
import com.codeshare.airline.inbound.domain.enums.RecordType;
import com.codeshare.airline.inbound.domain.enums.TimeMode;
import com.codeshare.airline.inbound.dto.common.ssim.*;
import com.codeshare.airline.ingestion.persistence.dto.common.ssim.*;
import com.codeshare.airline.inbound.dto.ssim.SSIMMessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class SsimMessageParser implements ScheduleParser<SSIMMessageDTO> {

    private static final int SSIM_RECORD_LENGTH = 200;

    @Override
    public SSIMMessageDTO parseMessage(ScheduleGroupedMessage scheduleGroupedMessage) {
        if (scheduleGroupedMessage.getOriginalLines() == null || scheduleGroupedMessage.getOriginalLines().isEmpty()) {
            throw new IllegalArgumentException("SSIM input is empty");
        }
        SSIMMessageDTO message = new SSIMMessageDTO();
        List<SsimFlightDTO> legs = new ArrayList<>();

        for (String line : scheduleGroupedMessage.getOriginalLines()) {

            char recordChar = line.charAt(0);

            RecordType recordType = RecordType.fromCode(Character.getNumericValue(recordChar));

            switch (recordType) {

                case HEADER ->  message.setHeader(processRecord1(line));

                case CARRIER -> message.setCarrier(processRecord2(line));

                case LEG -> {

                    SsimFlightDTO flightDTO = processRecord3(line);

                    message.getFlights().add(flightDTO);
                    // 🔥 Set flight context once
                    if (message.getFlightNumber() == null) {
                        message.setFlightNumber(flightDTO.getFlightNumber());
                        message.setAirlineCode(flightDTO.getAirlineCode());
                    }
                }

                case DEI -> attachDei(line, legs);

                case TRAILER -> message.setTrailer(processRecord5(line));

                default ->  log.warn("⚠️ Unknown SSIM record: {}", line);
            }
        }

        // 🔥 Final validation
        if (message.getFlights().isEmpty()) {
            log.warn("⚠️ No legs parsed for message");
        }

        return message;
    }

    private void attachDei(String line, List<SsimFlightDTO> legs) {

        if (legs.isEmpty()) {
            log.warn("DEI without leg: {}", line);
            return;
        }
        SsimFlightDTO lastLeg = legs.getLast();

        SsimDataElementDTO dei = processRecord4(line);

        if (dei != null) {
            lastLeg.getDeis().add(dei);
        }
    }
    /* ======================================================
       RECORD 1 - HEADER
       ====================================================== */

    private SsimHeaderDTO processRecord1(String line) {
        // 1. Validate record type
        if (line.charAt(0) != '1') {
            throw new IllegalArgumentException(
                    "Invalid Record Type. Expected '1', got: " + line.charAt(0)
            );
        }

        SsimHeaderDTO header = new SsimHeaderDTO();
        header.setRecordType(HEADER);

        // 2. Title (trimmed)
        header.setTitleOfContents(line.substring(1, 35).trim());

        // (Optional) Skip storing spare fields OR keep for audit
         header.setSpare36To40(line.substring(35, 40));
         header.setSpare42To191(line.substring(41, 191));

        // 4. Number of Seasons (SAFE parsing)
        String seasonsStr = line.substring(40, 41);

        if (seasonsStr.trim().isEmpty()) {
            // Real-world SSIM: sometimes blank → handle gracefully
            header.setNumberOfSeasons(null);
            // or default: header.setNumberOfSeasons(1);
        } else if (seasonsStr.matches("\\d")) {
            header.setNumberOfSeasons(Integer.parseInt(seasonsStr));
        } else {
            throw new IllegalArgumentException(
                    "Invalid Number of Seasons at pos 41: [" + seasonsStr + "]"
            );
        }

        // 5. Dataset Serial Number (keep as String, preserve leading zeros)
        String datasetSerial = line.substring(191, 194);
        if (!datasetSerial.matches("\\d{3}")) {
            throw new IllegalArgumentException(
                    "Invalid Dataset Serial Number: " + datasetSerial
            );
        }
        header.setDatasetSerialNumber(datasetSerial);

        // 6. Record Serial Number (keep as String)
        String recordSerial = line.substring(194, 200);
        if (!recordSerial.matches("\\d{6}")) {
            throw new IllegalArgumentException(
                    "Invalid Record Serial Number: " + recordSerial
            );
        }
        header.setRecordSerialNumber(recordSerial);

        return header;
    }

    /* ======================================================
       RECORD 2 - CARRIER
       ====================================================== */

    private SsimCarrierDTO processRecord2(String line) {

        SsimCarrierDTO carrier = new SsimCarrierDTO();
        carrier.setRecordType(CARRIER);

        String timeModeStr = line.substring(1, 2);

        TimeMode timeMode = null;
        if ("L".equals(timeModeStr)) {
            timeMode = TimeMode.LT;
        } else if ("U".equals(timeModeStr)) {
            timeMode = TimeMode.UTC;
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

        String recordSerial = line.substring(194, 200);
        safeNumeric(recordSerial);
        carrier.setRecordSerialNumber(safeNumeric(recordSerial));

        return carrier;
    }

    /* ======================================================
       RECORD 3 - FLIGHT LEG
       ====================================================== */

    private SsimFlightDTO processRecord3(String line) {

        SsimFlightDTO leg = new SsimFlightDTO();
        leg.setRecordType(LEG);

        leg.setOperationalSuffix(line.substring(1, 2));
        leg.setAirlineCode(line.substring(2, 5));
        leg.setFlightNumber(line.substring(5, 9));
        leg.setItineraryVariationIdentifier(line.substring(9, 11));
        leg.setLegSequenceNumber(Integer.valueOf(line.substring(11, 13)));
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

    /* ======================================================
       RECORD 4 - SEGMENT DEI
       ====================================================== */

    private SsimDataElementDTO processRecord4(String line) {

        SsimDataElementDTO dei = new SsimDataElementDTO();
        dei.setRecordType(DEI);

        dei.setOperationalSuffix(line.substring(1, 2));
        dei.setAirlineCode(line.substring(2, 5).trim());
        dei.setFlightNumber(line.substring(5, 9).trim());
        dei.setItineraryVariationIdentifier(line.substring(9, 11));

        dei.setLegSequenceNumber(line.substring(11, 13));

        dei.setServiceType(line.substring(13, 14));

        dei.setSpare15To27(line.substring(14, 27));
        dei.setItineraryVariationOverflow(line.substring(27, 28));

        dei.setBoardPointIndicator(line.substring(28, 29));
        dei.setOffPointIndicator(line.substring(29, 30));

        String deiNumber = line.substring(30, 33);
        safeNumeric(deiNumber);
        dei.setDataElementIdentifier(deiNumber);

        dei.setBoardPoint(line.substring(33, 36));
        dei.setOffPoint(line.substring(36, 39));

        dei.setDeiData(line.substring(39, 194));

        String recordSerial = line.substring(194, 200);
        safeNumeric(recordSerial);
        dei.setRecordSerialNumber(recordSerial);

        return dei;
    }

    /* ======================================================
       RECORD 5 - TRAILER
       ====================================================== */

    private SsimTrailerDTO processRecord5(String line) {

        SsimTrailerDTO trailer = new SsimTrailerDTO();

        trailer.setRecordType(RecordType.valueOf(line.substring(0, 1)));
        trailer.setSpareByte2(line.substring(1, 2));
        trailer.setAirlineDesignator(line.substring(2, 5));
        trailer.setReleaseDateRaw(line.substring(5, 12));
        trailer.setSpare13To187(line.substring(12, 187));
        trailer.setSerialCheckReference(line.substring(187, 193));
        trailer.setContinuationEndCode(line.substring(193, 194));
        trailer.setRecordSerialNumber(line.substring(194, 200));

        return trailer;
    }

    /* ======================================================
       HELPERS
       ====================================================== */

    private String safeNumeric(String value) {
        return value.chars().allMatch(Character::isDigit) ? value : null;
    }

    @Override
    public ScheduleGroupedMessage groupMessage(List<String> lines) {
          return new ScheduleGroupedMessage(
                null,       // groupedLines not needed for SSIM
                null,       // orderedLines not needed
                lines  // 🔥 this is what parser uses
        );
    }
}