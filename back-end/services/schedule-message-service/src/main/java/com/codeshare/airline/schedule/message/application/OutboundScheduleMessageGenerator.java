package com.codeshare.airline.schedule.message.application;

import com.codeshare.airline.platform.core.dto.schedule.workflow.ChangeSetDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleCodeshareSnapshotDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleDataElementSnapshotDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleFlightChangeDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleLegChangeDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleLegSnapshotDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleSegmentSnapshotDTO;
import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.platform.core.enums.schedule.ScheduleLegChangeType;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class OutboundScheduleMessageGenerator {

    private static final DateTimeFormatter MESSAGE_DATE = DateTimeFormatter.ofPattern("ddMMM", Locale.ENGLISH).withZone(ZoneOffset.UTC);
    private static final DateTimeFormatter IATA_DATE = DateTimeFormatter.ofPattern("ddMMMyy", Locale.ENGLISH);
    private static final DateTimeFormatter IATA_TIME = DateTimeFormatter.ofPattern("HHmm");
    private static final int TYPE_B_LINE_LIMIT = 69;
    private static final int SSIM_RECORD_LENGTH = 200;

    public String generate(ChangeSetDTO changeSet) {
        validateChangeSet(changeSet);
        if (changeSet.getMessageType() == MessageType.SSIM) {
            return generateSsim(changeSet);
        }
        if (changeSet.getMessageType() == MessageType.ASM) {
            return generateAsm(changeSet);
        }
        return generateSsm(changeSet);
    }

    private String generateSsm(ChangeSetDTO changeSet) {
        StringJoiner joiner = new StringJoiner(System.lineSeparator());
        joiner.add("SSM");
        joiner.add("UTC");
        appendLine(joiner, messageSequenceReference(changeSet));

        for (ScheduleFlightChangeDTO flightChange : changeSet.getFlightChanges()) {
            for (ScheduleLegChangeDTO legChange : flightChange.getLegChanges()) {
                ScheduleLegSnapshotDTO snapshot = preferredSnapshot(legChange);
                appendLine(joiner, ssmAction(legChange));
                appendLine(joiner, flightDesignator(flightChange));
                appendLine(joiner, periodLine(snapshot));
                if (requiresEquipmentLine(legChange.getChangeType())) {
                    appendLine(joiner, equipmentLine(snapshot));
                }
                if (requiresLegLine(legChange.getChangeType())) {
                    appendLine(joiner, legLine(snapshot));
                }
                appendDeiLines(joiner, snapshot);
                appendLine(joiner, "//");
            }
        }
        return joiner.toString();
    }

    private String generateAsm(ChangeSetDTO changeSet) {
        StringJoiner joiner = new StringJoiner(System.lineSeparator());
        joiner.add("ASM");
        joiner.add("UTC");
        appendLine(joiner, messageSequenceReference(changeSet));

        for (ScheduleFlightChangeDTO flightChange : changeSet.getFlightChanges()) {
            for (ScheduleLegChangeDTO legChange : flightChange.getLegChanges()) {
                ScheduleLegSnapshotDTO snapshot = preferredSnapshot(legChange);
                appendLine(joiner, asmAction(legChange));
                appendLine(joiner, flightIdentifier(flightChange, snapshot));
                if (requiresEquipmentLine(legChange.getChangeType())) {
                    appendLine(joiner, equipmentLine(snapshot));
                }
                if (requiresLegLine(legChange.getChangeType())) {
                    appendLine(joiner, legLine(snapshot));
                }
                appendDeiLines(joiner, snapshot);
                appendLine(joiner, "//");
            }
        }
        return joiner.toString();
    }

    private String generateSsim(ChangeSetDTO changeSet) {
        StringJoiner joiner = new StringJoiner(System.lineSeparator());
        AtomicInteger serial = new AtomicInteger(1);
        appendSsim(joiner, headerRecord(serial.getAndIncrement(), changeSet));
        appendZeroPadding(joiner, 4);
        appendSsim(joiner, carrierRecord(serial.getAndIncrement(), changeSet));
        appendZeroPadding(joiner, 4);

        int scheduleBlockCount = 0;

        for (ScheduleFlightChangeDTO flightChange : changeSet.getFlightChanges()) {
            List<ScheduleLegChangeDTO> sortedLegs = flightChange.getLegChanges().stream()
                    .sorted(Comparator.comparing(leg -> legSequence(preferredSnapshot(leg))))
                    .toList();

            for (ScheduleLegChangeDTO legChange : sortedLegs) {
                ScheduleLegSnapshotDTO snapshot = preferredSnapshot(legChange);
                appendSsim(joiner, flightRecord(serial.getAndIncrement(), flightChange, snapshot));
                scheduleBlockCount++;
                for (ScheduleDataElementSnapshotDTO dei : legDataElements(snapshot)) {
                    appendSsim(joiner, segmentDataRecord(serial.getAndIncrement(), flightChange, snapshot, dei));
                    scheduleBlockCount++;
                }
                for (ScheduleSegmentSnapshotDTO segment : segments(snapshot)) {
                    for (ScheduleDataElementSnapshotDTO dei : segment.getDataElements()) {
                        appendSsim(joiner, segmentDataRecord(serial.getAndIncrement(), flightChange, snapshot, segment, dei));
                        scheduleBlockCount++;
                    }
                }
                for (ScheduleCodeshareSnapshotDTO codeshare : codeshares(snapshot)) {
                    appendSsim(joiner, codeshareRecord(serial.getAndIncrement(), flightChange, snapshot, codeshare));
                    scheduleBlockCount++;
                }
            }
        }
        appendZeroPadding(joiner, paddingRecords(scheduleBlockCount));
        int previousSerial = serial.get() - 1;
        appendSsim(joiner, trailerRecord(serial.getAndIncrement(), changeSet, previousSerial));
        appendZeroPadding(joiner, 4);
        appendZeroPadding(joiner, 10);
        return joiner.toString();
    }

    private String ssmAction(ScheduleLegChangeDTO legChange) {
        return switch (changeType(legChange)) {
            case NEW, RIN -> "NEW";
            case CNL -> "CNL";
            case TIM -> "TIM";
            case EQT -> "EQT";
            case PER -> "REV";
            case COD -> "COD";
            case FLT -> "FLT";
        };
    }

    private String asmAction(ScheduleLegChangeDTO legChange) {
        return switch (changeType(legChange)) {
            case NEW -> "NEW SCHG";
            case CNL -> "CNL SCHG";
            case RIN -> "RIN SCHG";
            case TIM -> "TIM SCHG";
            case EQT -> "EQT SCHG";
            case PER -> "RPL SCHG";
            case COD -> "COD SCHG";
            case FLT -> "FLT SCHG";
        };
    }

    private ScheduleLegChangeType changeType(ScheduleLegChangeDTO legChange) {
        return legChange.getChangeType() == null ? ScheduleLegChangeType.FLT : legChange.getChangeType();
    }

    private boolean requiresLegLine(ScheduleLegChangeType changeType) {
        return switch (changeType == null ? ScheduleLegChangeType.FLT : changeType) {
            case NEW, RIN, TIM, PER, FLT, COD -> true;
            case CNL, EQT -> false;
        };
    }

    private boolean requiresEquipmentLine(ScheduleLegChangeType changeType) {
        return switch (changeType == null ? ScheduleLegChangeType.FLT : changeType) {
            case NEW, RIN, EQT, PER -> true;
            case CNL, TIM, FLT, COD -> false;
        };
    }

    private String messageSequenceReference(ChangeSetDTO changeSet) {
        Instant createdAt = changeSet.getCreatedAt() == null ? Instant.now() : changeSet.getCreatedAt();
        int hash = changeSet.getChangeSetId() == null ? 1 : Math.abs(changeSet.getChangeSetId().hashCode());
        String serial = String.format("%05d", hash % 100000);
        String reference = changeSet.getMessageReference();
        String line = MESSAGE_DATE.format(createdAt).toUpperCase(Locale.ENGLISH) + serial + "E001";
        if (reference != null && !reference.isBlank()) {
            line += "/REF " + reference.trim();
        }
        return line;
    }

    private String flightDesignator(ScheduleFlightChangeDTO flightChange) {
        return requiredAirlineDesignator(flightChange.getAirlineCode(), "flight airlineCode")
                + flightNumber(flightChange.getFlightNumber())
                + safe(flightChange.getOperationalSuffix());
    }

    private String flightIdentifier(ScheduleFlightChangeDTO flightChange, ScheduleLegSnapshotDTO snapshot) {
        return flightDesignator(flightChange) + "/" + iataDate(operationDate(snapshot));
    }

    private String periodLine(ScheduleLegSnapshotDTO snapshot) {
        return iataDate(requiredDate(snapshot != null ? snapshot.getPeriodStart() : null, "periodStart"))
                + " "
                + iataDate(requiredDate(snapshot != null ? snapshot.getPeriodEnd() : null, "periodEnd"))
                + " "
                + operatingDays(requiredText(snapshot != null ? snapshot.getDaysOfOperation() : null, "daysOfOperation"))
                + frequency(snapshot != null ? snapshot.getFrequencyRate() : null);
    }

    private String equipmentLine(ScheduleLegSnapshotDTO snapshot) {
        return serviceType(snapshot)
                + " "
                + aircraft(requiredText(snapshot != null ? snapshot.getAircraftType() : null, "aircraftType"))
                + " "
                + compact(snapshot != null ? snapshot.getBookingDesignator() : null)
                + bookingModifier(snapshot != null ? snapshot.getBookingModifier() : null)
                + aircraftConfiguration(snapshot != null ? snapshot.getAircraftConfiguration() : null);
    }

    private String legLine(ScheduleLegSnapshotDTO snapshot) {
        return requiredStation(snapshot != null ? snapshot.getDepartureStation() : null, "departureStation")
                + time(requiredTime(snapshot != null ? snapshot.getAircraftDepartureTime() : null, "aircraftDepartureTime"))
                + passengerTime(snapshot != null ? snapshot.getScheduledDepartureTime() : null, snapshot != null ? snapshot.getAircraftDepartureTime() : null)
                + " "
                + requiredStation(snapshot != null ? snapshot.getArrivalStation() : null, "arrivalStation")
                + time(requiredTime(snapshot != null ? snapshot.getAircraftArrivalTime() : null, "aircraftArrivalTime"))
                + dateVariationSuffix(snapshot);
    }

    private String dateVariationSuffix(ScheduleLegSnapshotDTO snapshot) {
        String value = snapshot == null ? null : snapshot.getDateVariation();
        return value == null || value.isBlank() ? "" : "/" + value.trim();
    }

    private void appendDeiLines(StringJoiner joiner, ScheduleLegSnapshotDTO snapshot) {
        for (ScheduleDataElementSnapshotDTO dei : legDataElements(snapshot)) {
            appendLine(joiner, deiLine(dei));
        }
        for (ScheduleSegmentSnapshotDTO segment : segments(snapshot)) {
            for (ScheduleDataElementSnapshotDTO dei : segment.getDataElements()) {
                appendLine(joiner, deiLine(segment, dei));
            }
        }
        for (ScheduleCodeshareSnapshotDTO codeshare : codeshares(snapshot)) {
            appendLine(joiner, codeshareLine(codeshare));
        }
    }

    private String deiLine(ScheduleDataElementSnapshotDTO dei) {
        return typeBDeiCode(dei.getCode()) + "/" + requiredText(dei.getValue(), "DEI value");
    }

    private String deiLine(ScheduleSegmentSnapshotDTO segment, ScheduleDataElementSnapshotDTO dei) {
        return requiredStation(segment.getBoardPoint(), "segment boardPoint")
                + requiredStation(segment.getOffPoint(), "segment offPoint")
                + " "
                + typeBDeiCode(dei.getCode())
                + "/"
                + requiredText(dei.getValue(), "segment DEI value");
    }

    private String codeshareLine(ScheduleCodeshareSnapshotDTO codeshare) {
        return requiredStation(codeshare.getBoardPoint(), "codeshare boardPoint")
                + requiredStation(codeshare.getOffPoint(), "codeshare offPoint")
                + " 10/"
                + requiredAirlineDesignator(codeshare.getMarketingAirlineCode(), "codeshare marketingAirlineCode")
                + flightNumber(codeshare.getMarketingFlightNumber())
                + safe(codeshare.getMarketingOperationalSuffix());
    }

    private String headerRecord(int serial, ChangeSetDTO changeSet) {
        char[] record = blankRecord('1');
        write(record, 1, 35, "AIRLINE STANDARD SCHEDULE DATA SET");
        write(record, 191, 194, dataSetSerial(changeSet));
        write(record, 194, 200, serial(serial));
        return new String(record);
    }

    private String carrierRecord(int serial, ChangeSetDTO changeSet) {
        char[] record = blankRecord('2');
        write(record, 1, 2, "U");
        write(record, 2, 5, requiredAirline(changeSet.getAirlineCode(), "airlineCode"));
        write(record, 10, 13, "CSM");
        LocalDate date = createdDate(changeSet);
        write(record, 14, 21, iataDate(date));
        write(record, 21, 28, iataDate(date.plusDays(365)));
        write(record, 28, 35, iataDate(date));
        write(record, 35, 64, "CODESHARE SCHEDULE");
        write(record, 71, 72, "P");
        write(record, 72, 107, safe(changeSet.getChangeSetId()));
        write(record, 190, 194, creationTime(changeSet));
        write(record, 194, 200, serial(serial));
        return new String(record);
    }

    private String flightRecord(int serial, ScheduleFlightChangeDTO flightChange, ScheduleLegSnapshotDTO snapshot) {
        char[] record = blankRecord('3');
        write(record, 1, 2, safe(flightChange.getOperationalSuffix()));
        write(record, 2, 5, requiredAirline(flightChange.getAirlineCode(), "flight airlineCode"));
        writeRight(record, 5, 9, flightNumber(flightChange.getFlightNumber()));
        write(record, 9, 11, twoDigit(snapshot != null && snapshot.getLegSequenceNumber() != null ? 1 : 1));
        write(record, 11, 13, twoDigit(legSequence(snapshot)));
        write(record, 13, 14, serviceType(snapshot));
        write(record, 14, 21, iataDate(requiredDate(snapshot != null ? snapshot.getPeriodStart() : null, "periodStart")));
        write(record, 21, 28, iataDate(requiredDate(snapshot != null ? snapshot.getPeriodEnd() : null, "periodEnd")));
        write(record, 28, 35, operatingDays(requiredText(snapshot != null ? snapshot.getDaysOfOperation() : null, "daysOfOperation")));
        write(record, 35, 36, safe(snapshot != null ? snapshot.getFrequencyRate() : null));
        write(record, 36, 39, requiredStation(snapshot != null ? snapshot.getDepartureStation() : null, "departureStation"));
        write(record, 39, 43, time(requiredTime(snapshot != null ? snapshot.getScheduledDepartureTime() : null, "scheduledDepartureTime")));
        write(record, 43, 47, time(requiredTime(snapshot != null ? snapshot.getAircraftDepartureTime() : null, "aircraftDepartureTime")));
        write(record, 47, 52, requiredUtcOffset(snapshot != null ? snapshot.getDepartureUtcOffset() : null, "departureUtcOffset"));
        write(record, 52, 54, safe(snapshot != null ? snapshot.getDepartureTerminal() : null));
        write(record, 54, 57, requiredStation(snapshot != null ? snapshot.getArrivalStation() : null, "arrivalStation"));
        write(record, 57, 61, time(requiredTime(snapshot != null ? snapshot.getAircraftArrivalTime() : null, "aircraftArrivalTime")));
        write(record, 61, 65, time(requiredTime(snapshot != null ? snapshot.getScheduledArrivalTime() : null, "scheduledArrivalTime")));
        write(record, 65, 70, requiredUtcOffset(snapshot != null ? snapshot.getArrivalUtcOffset() : null, "arrivalUtcOffset"));
        write(record, 70, 72, safe(snapshot != null ? snapshot.getArrivalTerminal() : null));
        write(record, 72, 75, aircraft(requiredText(snapshot != null ? snapshot.getAircraftType() : null, "aircraftType")));
        write(record, 75, 95, safe(snapshot != null ? snapshot.getBookingDesignator() : null));
        write(record, 95, 100, safe(snapshot != null ? snapshot.getBookingModifier() : null));
        write(record, 100, 110, safe(snapshot != null ? snapshot.getMealServiceNote() : null));
        write(record, 110, 119, safe(snapshot != null ? snapshot.getJointOperationAirlines() : null));
        write(record, 119, 121, safe(snapshot != null ? snapshot.getMinimumConnectingTimeStatus() : null));
        write(record, 121, 122, secureFlight(snapshot));
        write(record, 128, 131, airline(snapshot != null ? snapshot.getAircraftOwner() : null));
        write(record, 131, 134, airline(snapshot != null ? snapshot.getCockpitCrewEmployer() : null));
        write(record, 134, 137, airline(snapshot != null ? snapshot.getCabinCrewEmployer() : null));
        write(record, 137, 140, airline(snapshot != null ? snapshot.getOnwardAirlineDesignator() : null));
        writeRight(record, 140, 144, flightNumber(snapshot != null ? snapshot.getOnwardFlightNumber() : null));
        write(record, 144, 145, safe(snapshot != null ? snapshot.getAircraftRotationLayover() : null));
        write(record, 145, 146, safe(snapshot != null ? snapshot.getOnwardOperationalSuffix() : null));
        write(record, 147, 148, safe(snapshot != null ? snapshot.getFlightTransitLayover() : null));
        write(record, 148, 149, safe(snapshot != null ? snapshot.getOperatingAirlineDisclosure() : null));
        write(record, 149, 160, safe(snapshot != null ? snapshot.getTrafficRestrictionCode() : null));
        write(record, 172, 192, safe(snapshot != null ? snapshot.getAircraftConfiguration() : null));
        write(record, 192, 194, safe(snapshot != null ? snapshot.getDateVariation() : null));
        write(record, 194, 200, serial(serial));
        return new String(record);
    }

    private String segmentDataRecord(int serial,
                                     ScheduleFlightChangeDTO flightChange,
                                     ScheduleLegSnapshotDTO snapshot,
                                     ScheduleDataElementSnapshotDTO dei) {
        return segmentDataRecord(serial, flightChange, snapshot, null, dei);
    }

    private String segmentDataRecord(int serial,
                                     ScheduleFlightChangeDTO flightChange,
                                     ScheduleLegSnapshotDTO snapshot,
                                     ScheduleSegmentSnapshotDTO segment,
                                     ScheduleDataElementSnapshotDTO dei) {
        char[] record = segmentBaseRecord(serial, flightChange, snapshot);
        write(record, 28, 29, "B");
        write(record, 29, 30, "O");
        writeRight(record, 30, 33, code(dei.getCode()));
        write(record, 33, 36, requiredStation(boardPoint(segment, dei, snapshot), "DEI boardPoint"));
        write(record, 36, 39, requiredStation(offPoint(segment, dei, snapshot), "DEI offPoint"));
        write(record, 39, 194, requiredText(dei.getValue(), "DEI value"));
        return new String(record);
    }

    private String codeshareRecord(int serial,
                                   ScheduleFlightChangeDTO flightChange,
                                   ScheduleLegSnapshotDTO snapshot,
                                   ScheduleCodeshareSnapshotDTO codeshare) {
        char[] record = segmentBaseRecord(serial, flightChange, snapshot);
        write(record, 28, 29, "B");
        write(record, 29, 30, "O");
        writeRight(record, 30, 33, "010");
        write(record, 33, 36, requiredStation(codeshare.getBoardPoint(), "codeshare boardPoint"));
        write(record, 36, 39, requiredStation(codeshare.getOffPoint(), "codeshare offPoint"));
        write(record, 39, 194,
                requiredAirline(codeshare.getMarketingAirlineCode(), "codeshare marketingAirlineCode")
                        + flightNumber(codeshare.getMarketingFlightNumber())
                        + safe(codeshare.getMarketingOperationalSuffix())
                        + " "
                        + safe(codeshare.getMarketingBookingDesignator()));
        return new String(record);
    }

    private char[] segmentBaseRecord(int serial, ScheduleFlightChangeDTO flightChange, ScheduleLegSnapshotDTO snapshot) {
        char[] record = blankRecord('4');
        write(record, 1, 2, safe(flightChange.getOperationalSuffix()));
        write(record, 2, 5, requiredAirline(flightChange.getAirlineCode(), "flight airlineCode"));
        writeRight(record, 5, 9, flightNumber(flightChange.getFlightNumber()));
        write(record, 9, 11, "01");
        write(record, 11, 13, twoDigit(legSequence(snapshot)));
        write(record, 13, 14, serviceType(snapshot));
        write(record, 194, 200, serial(serial));
        return record;
    }

    private String trailerRecord(int serial, ChangeSetDTO changeSet, int previousSerial) {
        char[] record = blankRecord('5');
        write(record, 2, 5, requiredAirline(changeSet.getAirlineCode(), "airlineCode"));
        write(record, 187, 193, serial(previousSerial));
        write(record, 193, 194, "E");
        write(record, 194, 200, serial(serial));
        return new String(record);
    }

    private void validateChangeSet(ChangeSetDTO changeSet) {
        if (changeSet == null) {
            throw compliance("changeSet is required");
        }
        if (changeSet.getMessageType() == null) {
            throw compliance("messageType is required");
        }
        requiredAirline(changeSet.getAirlineCode(), "airlineCode");
        if (changeSet.getFlightChanges() == null || changeSet.getFlightChanges().isEmpty()) {
            throw compliance("at least one flight change is required");
        }

        for (ScheduleFlightChangeDTO flightChange : changeSet.getFlightChanges()) {
            requiredAirline(flightChange.getAirlineCode(), "flight airlineCode");
            requiredFlightNumber(flightChange.getFlightNumber(), "flightNumber");
            if (flightChange.getLegChanges() == null || flightChange.getLegChanges().isEmpty()) {
                throw compliance("at least one leg change is required for flight " + flightDesignator(flightChange));
            }
            for (ScheduleLegChangeDTO legChange : flightChange.getLegChanges()) {
                validateLegChange(changeSet.getMessageType(), legChange);
            }
        }
    }

    private void validateLegChange(MessageType messageType, ScheduleLegChangeDTO legChange) {
        if (legChange == null) {
            throw compliance("legChange is required");
        }
        ScheduleLegSnapshotDTO snapshot = preferredSnapshot(legChange);
        if (snapshot == null) {
            throw compliance("leg snapshot is required");
        }

        boolean chapter7 = messageType == MessageType.SSIM;
        if (chapter7 || legChange.getChangeType() != ScheduleLegChangeType.CNL) {
            requiredDate(snapshot.getPeriodStart(), "periodStart");
            requiredDate(snapshot.getPeriodEnd(), "periodEnd");
            requiredText(snapshot.getDaysOfOperation(), "daysOfOperation");
        }

        if (chapter7 || requiresLegLine(legChange.getChangeType())) {
            requiredStation(snapshot.getDepartureStation(), "departureStation");
            requiredStation(snapshot.getArrivalStation(), "arrivalStation");
            requiredTime(snapshot.getAircraftDepartureTime(), "aircraftDepartureTime");
            requiredTime(snapshot.getAircraftArrivalTime(), "aircraftArrivalTime");
        }

        if (chapter7) {
            requiredTime(snapshot.getScheduledDepartureTime(), "scheduledDepartureTime");
            requiredTime(snapshot.getScheduledArrivalTime(), "scheduledArrivalTime");
            requiredUtcOffset(snapshot.getDepartureUtcOffset(), "departureUtcOffset");
            requiredUtcOffset(snapshot.getArrivalUtcOffset(), "arrivalUtcOffset");
        }

        if (chapter7 || requiresEquipmentLine(legChange.getChangeType())) {
            requiredText(snapshot.getAircraftType(), "aircraftType");
            if (safe(snapshot.getBookingDesignator()).isBlank() && safe(snapshot.getAircraftConfiguration()).isBlank()) {
                throw compliance("bookingDesignator or aircraftConfiguration is required");
            }
            serviceType(snapshot);
        }

        for (ScheduleDataElementSnapshotDTO dei : legDataElements(snapshot)) {
            requiredCode(dei.getCode(), "DEI code");
            requiredText(dei.getValue(), "DEI value");
            requiredStation(boardPoint(null, dei, snapshot), "DEI boardPoint");
            requiredStation(offPoint(null, dei, snapshot), "DEI offPoint");
        }
        for (ScheduleSegmentSnapshotDTO segment : segments(snapshot)) {
            requiredStation(segment.getBoardPoint(), "segment boardPoint");
            requiredStation(segment.getOffPoint(), "segment offPoint");
            for (ScheduleDataElementSnapshotDTO dei : segment.getDataElements()) {
                requiredCode(dei.getCode(), "segment DEI code");
                requiredText(dei.getValue(), "segment DEI value");
            }
        }
        for (ScheduleCodeshareSnapshotDTO codeshare : codeshares(snapshot)) {
            requiredStation(codeshare.getBoardPoint(), "codeshare boardPoint");
            requiredStation(codeshare.getOffPoint(), "codeshare offPoint");
            requiredAirline(codeshare.getMarketingAirlineCode(), "codeshare marketingAirlineCode");
            requiredFlightNumber(codeshare.getMarketingFlightNumber(), "codeshare marketingFlightNumber");
        }
    }

    private void appendLine(StringJoiner joiner, String line) {
        String value = line == null ? "" : line.stripTrailing();
        if (value.length() <= TYPE_B_LINE_LIMIT) {
            joiner.add(value);
            return;
        }
        int start = 0;
        while (start < value.length()) {
            int end = Math.min(start + TYPE_B_LINE_LIMIT, value.length());
            joiner.add(value.substring(start, end).stripTrailing());
            start = end;
        }
    }

    private void appendSsim(StringJoiner joiner, String record) {
        if (record.length() != SSIM_RECORD_LENGTH) {
            throw new IllegalStateException("Invalid SSIM record length: " + record.length());
        }
        joiner.add(record);
    }

    private void appendZeroPadding(StringJoiner joiner, int count) {
        for (int i = 0; i < count; i++) {
            joiner.add("0".repeat(SSIM_RECORD_LENGTH));
        }
    }

    private int paddingRecords(int currentBlockCount) {
        int remainder = currentBlockCount % 5;
        return remainder == 0 ? 0 : 5 - remainder;
    }

    private char[] blankRecord(char recordType) {
        char[] record = " ".repeat(SSIM_RECORD_LENGTH).toCharArray();
        record[0] = recordType;
        return record;
    }

    private void write(char[] record, int start, int end, String value) {
        String padded = padRight(value, end - start);
        for (int i = 0; i < padded.length(); i++) {
            record[start + i] = padded.charAt(i);
        }
    }

    private void writeRight(char[] record, int start, int end, String value) {
        String padded = padLeft(value, end - start);
        for (int i = 0; i < padded.length(); i++) {
            record[start + i] = padded.charAt(i);
        }
    }

    private String dataSetSerial(ChangeSetDTO changeSet) {
        int hash = changeSet.getChangeSetId() == null ? 1 : Math.abs(changeSet.getChangeSetId().hashCode());
        return String.format("%03d", hash % 1000);
    }

    private String creationTime(ChangeSetDTO changeSet) {
        Instant createdAt = changeSet.getCreatedAt() == null ? Instant.now() : changeSet.getCreatedAt();
        return IATA_TIME.format(createdAt.atZone(ZoneOffset.UTC));
    }

    private LocalDate createdDate(ChangeSetDTO changeSet) {
        Instant createdAt = changeSet.getCreatedAt() == null ? Instant.now() : changeSet.getCreatedAt();
        return createdAt.atZone(ZoneOffset.UTC).toLocalDate();
    }

    private ScheduleLegSnapshotDTO preferredSnapshot(ScheduleLegChangeDTO legChange) {
        if (legChange == null) {
            return null;
        }
        return legChange.getNewValue() != null ? legChange.getNewValue() : legChange.getOldValue();
    }

    private LocalDate operationDate(ScheduleLegSnapshotDTO snapshot) {
        if (snapshot == null) {
            return LocalDate.now(ZoneOffset.UTC);
        }
        if (snapshot.getPeriodStart() != null) {
            return snapshot.getPeriodStart();
        }
        if (snapshot.getPeriodEnd() != null) {
            return snapshot.getPeriodEnd();
        }
        return LocalDate.now(ZoneOffset.UTC);
    }

    private List<ScheduleDataElementSnapshotDTO> legDataElements(ScheduleLegSnapshotDTO snapshot) {
        return snapshot == null || snapshot.getLegDataElements() == null ? List.of() : snapshot.getLegDataElements();
    }

    private List<ScheduleSegmentSnapshotDTO> segments(ScheduleLegSnapshotDTO snapshot) {
        return snapshot == null || snapshot.getSegments() == null ? List.of() : snapshot.getSegments();
    }

    private List<ScheduleCodeshareSnapshotDTO> codeshares(ScheduleLegSnapshotDTO snapshot) {
        return snapshot == null || snapshot.getCodeshares() == null ? List.of() : snapshot.getCodeshares();
    }

    private int legSequence(ScheduleLegSnapshotDTO snapshot) {
        return snapshot == null || snapshot.getLegSequenceNumber() == null ? 1 : snapshot.getLegSequenceNumber();
    }

    private String twoDigit(int value) {
        return String.format("%02d", Math.max(1, Math.min(99, value)));
    }

    private String serial(int value) {
        return String.format("%06d", Math.max(1, Math.min(999999, value)));
    }

    private String iataDate(LocalDate date) {
        return IATA_DATE.format(date == null ? LocalDate.now(ZoneOffset.UTC) : date).toUpperCase(Locale.ENGLISH);
    }

    private String time(LocalTime time) {
        return time == null ? "0000" : IATA_TIME.format(time);
    }

    private String operatingDays(String days) {
        String value = days == null || days.isBlank() ? "1234567" : days;
        value = value.replace('0', ' ');
        return padRight(value, 7);
    }

    private String frequency(String frequencyRate) {
        return frequencyRate == null || frequencyRate.isBlank() ? "" : "/W" + frequencyRate.trim();
    }

    private String serviceType(ScheduleLegSnapshotDTO snapshot) {
        String value = snapshot == null ? null : snapshot.getServiceType();
        return requiredText(value, "serviceType").substring(0, 1).toUpperCase(Locale.ENGLISH);
    }

    private String secureFlight(ScheduleLegSnapshotDTO snapshot) {
        String value = snapshot == null ? null : snapshot.getSecureFlightIndicator();
        return "S".equalsIgnoreCase(value) || "Y".equalsIgnoreCase(value) ? "S" : "";
    }

    private String station(String value) {
        return safe(value).isBlank() ? "XXX" : safe(value).toUpperCase(Locale.ENGLISH);
    }

    private String airline(String value) {
        return padRight(safe(value).toUpperCase(Locale.ENGLISH), 3);
    }

    private String aircraft(String value) {
        String safe = safe(value).toUpperCase(Locale.ENGLISH);
        return safe.isBlank() ? "ZZZ" : safe;
    }

    private String flightNumber(String value) {
        return safe(value).replaceAll("[^A-Za-z0-9]", "");
    }

    private String code(String value) {
        String safe = safe(value).replaceAll("[^0-9]", "");
        if (safe.isBlank()) {
            return "999";
        }
        return String.format("%03d", Integer.parseInt(safe));
    }

    private String typeBDeiCode(String value) {
        String safe = requiredCode(value, "DEI code");
        int code = Integer.parseInt(safe);
        if (code < 1 || code > 999) {
            throw compliance("DEI code must be between 1 and 999");
        }
        return Integer.toString(code);
    }

    private String utcOffset(String value) {
        String safe = safe(value);
        if (safe.matches("[+-]\\d{4}")) {
            return safe;
        }
        if (safe.matches("[+-]\\d{2}:\\d{2}")) {
            return safe.replace(":", "");
        }
        return "+0000";
    }

    private String bookingModifier(String value) {
        String safe = compact(value);
        return safe.isBlank() ? "" : "/" + safe;
    }

    private String aircraftConfiguration(String value) {
        String safe = compact(value);
        return safe.isBlank() ? "" : "." + safe;
    }

    private String passengerTime(LocalTime passengerTime, LocalTime aircraftTime) {
        if (passengerTime == null || aircraftTime == null || passengerTime.equals(aircraftTime)) {
            return "";
        }
        return "/" + time(passengerTime);
    }

    private String boardPoint(ScheduleSegmentSnapshotDTO segment, ScheduleDataElementSnapshotDTO dei, ScheduleLegSnapshotDTO snapshot) {
        if (segment != null && !safe(segment.getBoardPoint()).isBlank()) {
            return segment.getBoardPoint();
        }
        if (dei != null && !safe(dei.getBoardPoint()).isBlank()) {
            return dei.getBoardPoint();
        }
        return snapshot == null ? null : snapshot.getDepartureStation();
    }

    private String offPoint(ScheduleSegmentSnapshotDTO segment, ScheduleDataElementSnapshotDTO dei, ScheduleLegSnapshotDTO snapshot) {
        if (segment != null && !safe(segment.getOffPoint()).isBlank()) {
            return segment.getOffPoint();
        }
        if (dei != null && !safe(dei.getOffPoint()).isBlank()) {
            return dei.getOffPoint();
        }
        return snapshot == null ? null : snapshot.getArrivalStation();
    }

    private String requiredText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw compliance(field + " is required");
        }
        return value.trim();
    }

    private LocalDate requiredDate(LocalDate value, String field) {
        if (value == null) {
            throw compliance(field + " is required");
        }
        return value;
    }

    private LocalTime requiredTime(LocalTime value, String field) {
        if (value == null) {
            throw compliance(field + " is required");
        }
        return value;
    }

    private String requiredStation(String value, String field) {
        String safe = requiredText(value, field).toUpperCase(Locale.ENGLISH);
        if (!safe.matches("[A-Z]{3}")) {
            throw compliance(field + " must be a 3-letter IATA station code");
        }
        return safe;
    }

    private String requiredAirline(String value, String field) {
        String safe = requiredText(value, field).toUpperCase(Locale.ENGLISH);
        if (!safe.matches("[A-Z0-9]{2,3}")) {
            throw compliance(field + " must be a 2 or 3 character airline designator");
        }
        return padRight(safe, 3);
    }

    private String requiredAirlineDesignator(String value, String field) {
        String safe = requiredText(value, field).toUpperCase(Locale.ENGLISH);
        if (!safe.matches("[A-Z0-9]{2,3}")) {
            throw compliance(field + " must be a 2 or 3 character airline designator");
        }
        return safe;
    }

    private String requiredFlightNumber(String value, String field) {
        String safe = flightNumber(requiredText(value, field));
        if (!safe.matches("[A-Za-z0-9]{1,4}")) {
            throw compliance(field + " must be 1 to 4 alphanumeric characters");
        }
        return safe;
    }

    private String requiredCode(String value, String field) {
        String safe = requiredText(value, field).replaceAll("[^0-9]", "");
        if (!safe.matches("\\d{1,3}")) {
            throw compliance(field + " must be a numeric DEI code");
        }
        return safe;
    }

    private String requiredUtcOffset(String value, String field) {
        String safe = requiredText(value, field);
        if (safe.matches("[+-]\\d{2}:\\d{2}")) {
            return safe.replace(":", "");
        }
        if (safe.matches("[+-]\\d{4}")) {
            return safe;
        }
        throw compliance(field + " must be in +HHMM or +HH:MM format");
    }

    private OutboundScheduleMessageComplianceException compliance(String message) {
        return new OutboundScheduleMessageComplianceException("Outbound IATA compliance failure: " + message);
    }

    private String compact(String value) {
        return safe(value).replace(" ", "");
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }

    private String safe(Object value) {
        return value == null ? "" : value.toString().trim();
    }

    private String padRight(String value, int length) {
        String safe = safe(value);
        return safe.length() >= length ? safe.substring(0, length) : String.format("%-" + length + "s", safe);
    }

    private String padLeft(String value, int length) {
        String safe = safe(value);
        return safe.length() >= length ? safe.substring(0, length) : String.format("%" + length + "s", safe);
    }
}
