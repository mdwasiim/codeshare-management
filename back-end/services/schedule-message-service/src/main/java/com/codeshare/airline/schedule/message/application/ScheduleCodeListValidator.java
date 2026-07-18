package com.codeshare.airline.schedule.message.application;

import com.codeshare.airline.platform.core.dto.master.validation.ScheduleCodeListValidationErrorDTO;
import com.codeshare.airline.platform.core.dto.master.validation.ScheduleCodeListValidationRequestDTO;
import com.codeshare.airline.platform.core.dto.master.validation.ScheduleCodeListValidationResponseDTO;
import com.codeshare.airline.platform.core.dto.master.validation.TerminalCodeDTO;
import com.codeshare.airline.platform.core.dto.master.validation.TrafficRestrictionQualifierCodeDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ChangeSetDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleCodeshareSnapshotDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleDataElementSnapshotDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleFlightChangeDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleLegChangeDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleLegSnapshotDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleSegmentSnapshotDTO;
import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.message.feign.MasterDataScheduleCodeListClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class ScheduleCodeListValidator {

    private final MasterDataScheduleCodeListClient client;

    public void validate(ChangeSetDTO changeSet) {
        ScheduleCodeListValidationResponseDTO response = client.validate(request(changeSet));
        if (response == null || response.isValid()) {
            return;
        }
        throw new OutboundScheduleMessageComplianceException("Outbound master-data validation failure: "
                + formatErrors(response.getErrors()));
    }

    private ScheduleCodeListValidationRequestDTO request(ChangeSetDTO changeSet) {
        ScheduleCodeListValidationRequestDTO request = new ScheduleCodeListValidationRequestDTO();
        if (changeSet == null) {
            return request;
        }

        addMessageIdentifier(request, changeSet.getMessageType());
        request.setOperatingAirlineCode(normalize(changeSet.getAirlineCode()));
        add(request.getAirlineCodes(), changeSet.getAirlineCode());
        add(request.getAirlineCodes(), changeSet.getPartnerCode());

        for (ScheduleFlightChangeDTO flightChange : list(changeSet.getFlightChanges())) {
            addFlight(request, flightChange);
        }
        return request;
    }

    private void addFlight(ScheduleCodeListValidationRequestDTO request, ScheduleFlightChangeDTO flightChange) {
        if (flightChange == null) {
            return;
        }
        add(request.getAirlineCodes(), flightChange.getAirlineCode());
        addSuffix(request, flightChange.getOperationalSuffix());
        for (ScheduleLegChangeDTO legChange : list(flightChange.getLegChanges())) {
            if (legChange != null) {
                add(request.getActionCodes(), actionCode(legChange));
                addSnapshot(request, legChange.getOldValue());
                addSnapshot(request, legChange.getNewValue());
            }
        }
    }

    private void addMessageIdentifier(ScheduleCodeListValidationRequestDTO request, MessageType messageType) {
        if (messageType == MessageType.SSM || messageType == MessageType.ASM) {
            add(request.getMessageIdentifiers(), messageType.name());
        }
    }

    private void addSnapshot(ScheduleCodeListValidationRequestDTO request, ScheduleLegSnapshotDTO snapshot) {
        if (snapshot == null) {
            return;
        }

        add(request.getAirportCodes(), snapshot.getDepartureStation());
        add(request.getAirportCodes(), snapshot.getArrivalStation());
        add(request.getAircraftTypeCodes(), snapshot.getAircraftType());
        add(request.getAircraftConfigurationCodes(), snapshot.getAircraftConfiguration());
        add(request.getServiceTypeCodes(), snapshot.getServiceType());
        addEachCharacter(request.getMealServiceCodes(), snapshot.getMealServiceNote());
        add(request.getSecureFlightIndicatorCodes(), secureFlightIndicator(snapshot.getSecureFlightIndicator()));
        addEachCharacter(request.getReservationBookingDesignators(), snapshot.getBookingDesignator());
        add(request.getReservationBookingModifiers(), snapshot.getBookingModifier());
        add(request.getAirlineCodes(), snapshot.getAircraftOwner());
        add(request.getAirlineCodes(), snapshot.getCockpitCrewEmployer());
        add(request.getAirlineCodes(), snapshot.getCabinCrewEmployer());
        add(request.getAirlineCodes(), snapshot.getOnwardAirlineDesignator());
        addSuffix(request, snapshot.getOnwardOperationalSuffix());
        addTrafficRestriction(request, snapshot.getTrafficRestrictionCode());
        addTerminal(request, snapshot.getDepartureStation(), snapshot.getDepartureTerminal());
        addTerminal(request, snapshot.getArrivalStation(), snapshot.getArrivalTerminal());

        for (ScheduleDataElementSnapshotDTO dataElement : list(snapshot.getLegDataElements())) {
            addDataElement(request, dataElement);
        }
        for (ScheduleSegmentSnapshotDTO segment : list(snapshot.getSegments())) {
            add(request.getAirportCodes(), segment.getBoardPoint());
            add(request.getAirportCodes(), segment.getOffPoint());
            for (ScheduleDataElementSnapshotDTO dataElement : list(segment.getDataElements())) {
                addDataElement(request, dataElement);
            }
        }
        for (ScheduleCodeshareSnapshotDTO codeshare : list(snapshot.getCodeshares())) {
            add(request.getAirportCodes(), codeshare.getBoardPoint());
            add(request.getAirportCodes(), codeshare.getOffPoint());
            add(request.getAirlineCodes(), codeshare.getMarketingAirlineCode());
            addEachCharacter(request.getReservationBookingDesignators(), codeshare.getMarketingBookingDesignator());
            addSuffix(request, codeshare.getMarketingOperationalSuffix());
            add(request.getDeiNumbers(), codeshare.getSourceDeiCode());
        }
    }

    private void addDataElement(ScheduleCodeListValidationRequestDTO request, ScheduleDataElementSnapshotDTO dataElement) {
        if (dataElement == null) {
            return;
        }
        add(request.getDeiNumbers(), dataElement.getCode());
        add(request.getAirportCodes(), dataElement.getBoardPoint());
        add(request.getAirportCodes(), dataElement.getOffPoint());
    }

    private void addTerminal(ScheduleCodeListValidationRequestDTO request, String airportCode, String terminalCode) {
        String normalizedAirport = normalize(airportCode);
        String normalizedTerminal = normalize(terminalCode);
        if (normalizedAirport == null || normalizedTerminal == null) {
            return;
        }

        String key = normalizedAirport + "/" + normalizedTerminal;
        boolean exists = request.getTerminalCodes().stream()
                .anyMatch(terminal -> key.equals(normalize(terminal.getAirportCode()) + "/" + normalize(terminal.getTerminalCode())));
        if (!exists) {
            TerminalCodeDTO terminal = new TerminalCodeDTO();
            terminal.setAirportCode(normalizedAirport);
            terminal.setTerminalCode(normalizedTerminal);
            request.getTerminalCodes().add(terminal);
        }
    }

    private void addSuffix(ScheduleCodeListValidationRequestDTO request, String suffix) {
        add(request.getOperationalSuffixCodes(), suffix);
        add(request.getFlightSuffixCodes(), suffix);
    }

    private void addTrafficRestriction(ScheduleCodeListValidationRequestDTO request, String value) {
        String normalized = normalize(value);
        if (normalized == null) {
            return;
        }

        for (String token : normalized.split("[,;|]+")) {
            String compact = token.replace("/", "");
            if (compact.isBlank()) {
                continue;
            }
            if (compact.length() == 1) {
                add(request.getTrafficRestrictionCodes(), compact);
                continue;
            }

            String restrictionCode = compact.substring(0, 1);
            String qualifierCode = compact.substring(1);
            add(request.getTrafficRestrictionCodes(), restrictionCode);
            addTrafficRestrictionQualifier(request, restrictionCode, qualifierCode);
        }
    }

    private void addTrafficRestrictionQualifier(ScheduleCodeListValidationRequestDTO request,
                                                String restrictionCode,
                                                String qualifierCode) {

        String key = restrictionCode + "/" + qualifierCode;
        boolean exists = request.getTrafficRestrictionQualifiers().stream()
                .anyMatch(qualifier -> key.equals(normalize(qualifier.getRestrictionCode()) + "/"
                        + normalize(qualifier.getQualifierCode())));
        if (!exists) {
            TrafficRestrictionQualifierCodeDTO qualifier = new TrafficRestrictionQualifierCodeDTO();
            qualifier.setRestrictionCode(restrictionCode);
            qualifier.setQualifierCode(qualifierCode);
            request.getTrafficRestrictionQualifiers().add(qualifier);
        }
    }

    private String actionCode(ScheduleLegChangeDTO legChange) {
        return switch (legChange.getChangeType() == null ? "" : legChange.getChangeType().name()) {
            case "NEW", "CNL", "TIM", "EQT", "RIN" -> legChange.getChangeType().name();
            case "PER" -> "REV";
            case "FLT", "COD" -> legChange.getChangeType().name();
            default -> null;
        };
    }

    private void addEachCharacter(List<String> values, String value) {
        String normalized = normalize(value);
        if (normalized == null) {
            return;
        }
        for (int i = 0; i < normalized.length(); i++) {
            add(values, Character.toString(normalized.charAt(i)));
        }
    }

    private void add(List<String> values, String value) {
        String normalized = normalize(value);
        if (normalized != null && !values.contains(normalized)) {
            values.add(normalized);
        }
    }

    private String secureFlightIndicator(String value) {
        String normalized = normalize(value);
        if (normalized == null) {
            return null;
        }
        return "S".equals(normalized) ? "Y" : normalized;
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim().toUpperCase(Locale.ENGLISH).replace(" ", "");
    }

    private <T> List<T> list(List<T> values) {
        return values == null ? List.of() : values;
    }

    private String formatErrors(List<ScheduleCodeListValidationErrorDTO> errors) {
        if (errors == null || errors.isEmpty()) {
            return "unknown master-data code-list validation error";
        }
        return errors.stream()
                .map(error -> error.getField() + "=" + error.getCode() + " (" + error.getMessage() + ")")
                .toList()
                .toString();
    }
}
