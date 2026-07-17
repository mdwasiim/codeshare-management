package com.codeshare.airline.schedule.processing.application.validation;

import com.codeshare.airline.platform.core.dto.schedule.workflow.ImportedScheduleDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleCodeshareSnapshotDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleDataElementSnapshotDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleFlightSnapshotDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleLegSnapshotDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleSegmentSnapshotDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ScheduleBusinessValidationService {

    public BusinessValidationResult validate(ImportedScheduleDTO schedule) {
        List<BusinessValidationIssue> issues = new ArrayList<>();

        if (schedule == null) {
            issues.add(issue("PROC_BUS_000", "Imported schedule is required", "SCHEDULE", null));
            return new BusinessValidationResult(List.copyOf(issues));
        }

        require(schedule.getImportedScheduleId(), "PROC_BUS_001", "Imported schedule id is required", "SCHEDULE", null, issues);
        require(schedule.getImportBatchId(), "PROC_BUS_002", "Import batch id is required", "SCHEDULE", null, issues);
        requireText(schedule.getAirlineCode(), "PROC_BUS_003", "Airline code is required", "SCHEDULE", key(schedule.getImportedScheduleId()), issues);
        require(schedule.getMessageType(), "PROC_BUS_004", "Message type is required", "SCHEDULE", key(schedule.getImportedScheduleId()), issues);

        if (schedule.getFlights() == null || schedule.getFlights().isEmpty()) {
            issues.add(issue("PROC_BUS_005", "Imported schedule must contain at least one flight", "SCHEDULE", key(schedule.getImportedScheduleId())));
            return new BusinessValidationResult(List.copyOf(issues));
        }

        Set<String> flightKeys = new HashSet<>();
        for (ScheduleFlightSnapshotDTO flight : schedule.getFlights()) {
            validateFlight(schedule, flight, flightKeys, issues);
        }

        return new BusinessValidationResult(List.copyOf(issues));
    }

    private void validateFlight(
            ImportedScheduleDTO schedule,
            ScheduleFlightSnapshotDTO flight,
            Set<String> flightKeys,
            List<BusinessValidationIssue> issues
    ) {
        if (flight == null) {
            issues.add(issue("PROC_BUS_010", "Flight record is required", "FLIGHT", null));
            return;
        }

        String flightKey = flightKey(flight);
        requireText(flight.getAirlineCode(), "PROC_BUS_011", "Operating airline is required", "FLIGHT", flightKey, issues);
        requireText(flight.getFlightNumber(), "PROC_BUS_012", "Flight number is required", "FLIGHT", flightKey, issues);

        if (hasText(schedule.getAirlineCode()) && hasText(flight.getAirlineCode())
                && !schedule.getAirlineCode().equalsIgnoreCase(flight.getAirlineCode())) {
            issues.add(issue("PROC_BUS_013", "Flight airline must match imported schedule airline", "FLIGHT", flightKey));
        }

        if (!flightKeys.add(flightKey)) {
            issues.add(issue("PROC_BUS_014", "Duplicate flight identity in imported schedule", "FLIGHT", flightKey));
        }

        if (flight.getLegs() == null || flight.getLegs().isEmpty()) {
            issues.add(issue("PROC_BUS_015", "Flight must contain at least one leg", "FLIGHT", flightKey));
            return;
        }

        Set<String> legKeys = new HashSet<>();
        for (ScheduleLegSnapshotDTO leg : flight.getLegs()) {
            validateLeg(flightKey, leg, legKeys, issues);
        }
    }

    private void validateLeg(
            String flightKey,
            ScheduleLegSnapshotDTO leg,
            Set<String> legKeys,
            List<BusinessValidationIssue> issues
    ) {
        if (leg == null) {
            issues.add(issue("PROC_BUS_020", "Leg record is required", "LEG", flightKey));
            return;
        }

        String legKey = flightKey + "|" + nullToBlank(leg.getLegSequenceNumber());
        require(leg.getLegSequenceNumber(), "PROC_BUS_021", "Leg sequence number is required", "LEG", legKey, issues);
        require(leg.getPeriodStart(), "PROC_BUS_022", "Period start is required", "LEG", legKey, issues);
        require(leg.getPeriodEnd(), "PROC_BUS_023", "Period end is required", "LEG", legKey, issues);
        requireText(leg.getDaysOfOperation(), "PROC_BUS_024", "Days of operation is required", "LEG", legKey, issues);
        requireText(leg.getDepartureStation(), "PROC_BUS_025", "Departure station is required", "LEG", legKey, issues);
        requireText(leg.getArrivalStation(), "PROC_BUS_026", "Arrival station is required", "LEG", legKey, issues);
        require(leg.getScheduledDepartureTime(), "PROC_BUS_027", "Scheduled departure time is required", "LEG", legKey, issues);
        require(leg.getScheduledArrivalTime(), "PROC_BUS_028", "Scheduled arrival time is required", "LEG", legKey, issues);
        requireText(leg.getServiceType(), "PROC_BUS_029", "Service type is required", "LEG", legKey, issues);
        requireText(leg.getAircraftType(), "PROC_BUS_030", "Aircraft type is required", "LEG", legKey, issues);

        if (leg.getPeriodStart() != null && leg.getPeriodEnd() != null && leg.getPeriodStart().isAfter(leg.getPeriodEnd())) {
            issues.add(issue("PROC_BUS_031", "Period start cannot be after period end", "LEG", legKey));
        }

        if (sameText(leg.getDepartureStation(), leg.getArrivalStation())) {
            issues.add(issue("PROC_BUS_032", "Departure and arrival stations cannot be the same", "LEG", legKey));
        }

        if (leg.getScheduledDepartureTime() != null
                && leg.getScheduledArrivalTime() != null
                && sameDayLeg(leg.getDateVariation())
                && !leg.getScheduledArrivalTime().isAfter(leg.getScheduledDepartureTime())) {
            issues.add(issue("PROC_BUS_033", "Same-day leg arrival time must be after departure time", "LEG", legKey));
        }

        if (!legKeys.add(legIdentityKey(leg))) {
            issues.add(issue("PROC_BUS_034", "Duplicate leg identity in flight", "LEG", legKey));
        }

        validateDataElements("LEG", legKey, leg.getLegDataElements(), issues);
        validateSegments(legKey, leg, issues);
        validateCodeshares(legKey, leg.getCodeshares(), issues);
    }

    private void validateSegments(String legKey, ScheduleLegSnapshotDTO leg, List<BusinessValidationIssue> issues) {
        if (leg.getSegments() == null) {
            return;
        }

        Set<String> segmentKeys = new HashSet<>();
        for (ScheduleSegmentSnapshotDTO segment : leg.getSegments()) {
            if (segment == null) {
                issues.add(issue("PROC_BUS_040", "Segment record is required", "SEGMENT", legKey));
                continue;
            }

            String segmentKey = legKey + "|" + nullToBlank(segment.getBoardPoint()) + "-" + nullToBlank(segment.getOffPoint());
            requireText(segment.getBoardPoint(), "PROC_BUS_041", "Segment board point is required", "SEGMENT", segmentKey, issues);
            requireText(segment.getOffPoint(), "PROC_BUS_042", "Segment off point is required", "SEGMENT", segmentKey, issues);
            if (sameText(segment.getBoardPoint(), segment.getOffPoint())) {
                issues.add(issue("PROC_BUS_043", "Segment board and off points cannot be the same", "SEGMENT", segmentKey));
            }
            if (!segmentKeys.add(nullToBlank(segment.getBoardPoint()) + "|" + nullToBlank(segment.getOffPoint()))) {
                issues.add(issue("PROC_BUS_044", "Duplicate segment in leg", "SEGMENT", segmentKey));
            }
            validateDataElements("SEGMENT", segmentKey, segment.getDataElements(), issues);
        }
    }

    private void validateDataElements(
            String recordType,
            String parentKey,
            List<ScheduleDataElementSnapshotDTO> dataElements,
            List<BusinessValidationIssue> issues
    ) {
        if (dataElements == null) {
            return;
        }

        Set<String> deiKeys = new HashSet<>();
        for (ScheduleDataElementSnapshotDTO dei : dataElements) {
            if (dei == null) {
                issues.add(issue("PROC_BUS_050", "Data element record is required", recordType, parentKey));
                continue;
            }

            String deiKey = parentKey + "|" + nullToBlank(dei.getCode()) + "|" + nullToBlank(dei.getSequenceOrder());
            requireText(dei.getCode(), "PROC_BUS_051", "Data element code is required", recordType, deiKey, issues);
            if (dei.getSequenceOrder() != null && dei.getSequenceOrder() < 1) {
                issues.add(issue("PROC_BUS_052", "Data element sequence order must be positive", recordType, deiKey));
            }
            if (!deiKeys.add(nullToBlank(dei.getCode()) + "|" + nullToBlank(dei.getSequenceOrder())
                    + "|" + nullToBlank(dei.getBoardPoint()) + "|" + nullToBlank(dei.getOffPoint()))) {
                issues.add(issue("PROC_BUS_053", "Duplicate data element in record", recordType, deiKey));
            }
        }
    }

    private void validateCodeshares(
            String legKey,
            List<ScheduleCodeshareSnapshotDTO> codeshares,
            List<BusinessValidationIssue> issues
    ) {
        if (codeshares == null) {
            return;
        }

        Set<String> codeshareKeys = new HashSet<>();
        for (ScheduleCodeshareSnapshotDTO codeshare : codeshares) {
            if (codeshare == null) {
                issues.add(issue("PROC_BUS_060", "Codeshare record is required", "CODESHARE", legKey));
                continue;
            }

            String codeshareKey = legKey + "|" + nullToBlank(codeshare.getMarketingAirlineCode())
                    + "|" + nullToBlank(codeshare.getMarketingFlightNumber());
            requireText(codeshare.getMarketingAirlineCode(), "PROC_BUS_061", "Marketing airline is required", "CODESHARE", codeshareKey, issues);
            requireText(codeshare.getMarketingFlightNumber(), "PROC_BUS_062", "Marketing flight number is required", "CODESHARE", codeshareKey, issues);
            if (!codeshareKeys.add(codeshareKey)) {
                issues.add(issue("PROC_BUS_063", "Duplicate codeshare in leg", "CODESHARE", codeshareKey));
            }
        }
    }

    private boolean sameDayLeg(String dateVariation) {
        return dateVariation == null || dateVariation.isBlank() || "0".equals(dateVariation.trim());
    }

    private String flightKey(ScheduleFlightSnapshotDTO flight) {
        return nullToBlank(flight.getAirlineCode()) + "|" + nullToBlank(flight.getFlightNumber())
                + "|" + nullToBlank(flight.getOperationalSuffix()) + "|" + nullToBlank(flight.getItineraryVariationId());
    }

    private String legIdentityKey(ScheduleLegSnapshotDTO leg) {
        LocalDate start = leg.getPeriodStart();
        LocalDate end = leg.getPeriodEnd();
        LocalTime departure = leg.getScheduledDepartureTime();
        return nullToBlank(leg.getLegSequenceNumber()) + "|" + nullToBlank(start) + "|" + nullToBlank(end)
                + "|" + nullToBlank(leg.getDaysOfOperation()) + "|" + nullToBlank(leg.getDepartureStation())
                + "|" + nullToBlank(leg.getArrivalStation()) + "|" + nullToBlank(departure);
    }

    private void require(Object value, String code, String message, String recordType, String recordKey, List<BusinessValidationIssue> issues) {
        if (value == null) {
            issues.add(issue(code, message, recordType, recordKey));
        }
    }

    private void requireText(String value, String code, String message, String recordType, String recordKey, List<BusinessValidationIssue> issues) {
        if (!hasText(value)) {
            issues.add(issue(code, message, recordType, recordKey));
        }
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private boolean sameText(String left, String right) {
        return hasText(left) && hasText(right) && left.trim().equalsIgnoreCase(right.trim());
    }

    private String key(Object value) {
        return value == null ? null : value.toString();
    }

    private String nullToBlank(Object value) {
        return value == null ? "" : value.toString().trim();
    }

    private BusinessValidationIssue issue(String code, String message, String recordType, String recordKey) {
        return new BusinessValidationIssue(code, message, recordType, recordKey);
    }
}
