package com.codeshare.airline.schedule.message.application;

import com.codeshare.airline.platform.core.dto.master.validation.ScheduleTimeValidationErrorDTO;
import com.codeshare.airline.platform.core.dto.master.validation.ScheduleTimeValidationLegDTO;
import com.codeshare.airline.platform.core.dto.master.validation.ScheduleTimeValidationRequestDTO;
import com.codeshare.airline.platform.core.dto.master.validation.ScheduleTimeValidationResponseDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ChangeSetDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleFlightChangeDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleLegChangeDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.ScheduleLegSnapshotDTO;
import com.codeshare.airline.schedule.message.feign.MasterDataScheduleTimeClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ScheduleTimeValidator {

    private final MasterDataScheduleTimeClient client;

    public void validate(ChangeSetDTO changeSet) {
        ScheduleTimeValidationRequestDTO request = request(changeSet);
        if (request.getLegs().isEmpty()) {
            return;
        }
        ScheduleTimeValidationResponseDTO response = client.validate(request);
        if (response == null || response.isValid()) {
            return;
        }
        throw new OutboundScheduleMessageComplianceException("Outbound schedule time validation failure: "
                + formatErrors(response.getErrors()));
    }

    private ScheduleTimeValidationRequestDTO request(ChangeSetDTO changeSet) {
        ScheduleTimeValidationRequestDTO request = new ScheduleTimeValidationRequestDTO();
        if (changeSet == null) {
            return request;
        }

        for (ScheduleFlightChangeDTO flightChange : list(changeSet.getFlightChanges())) {
            if (flightChange == null) {
                continue;
            }
            for (ScheduleLegChangeDTO legChange : list(flightChange.getLegChanges())) {
                addLeg(request, legChange != null ? legChange.getOldValue() : null);
                addLeg(request, legChange != null ? legChange.getNewValue() : null);
            }
        }
        return request;
    }

    private void addLeg(ScheduleTimeValidationRequestDTO request, ScheduleLegSnapshotDTO snapshot) {
        if (snapshot == null
                || snapshot.getPeriodStart() == null
                || snapshot.getAircraftDepartureTime() == null
                || snapshot.getAircraftArrivalTime() == null) {
            return;
        }

        ScheduleTimeValidationLegDTO leg = new ScheduleTimeValidationLegDTO();
        leg.setDepartureAirport(snapshot.getDepartureStation());
        leg.setArrivalAirport(snapshot.getArrivalStation());
        leg.setDepartureDate(snapshot.getPeriodStart());
        leg.setDepartureTime(snapshot.getAircraftDepartureTime());
        leg.setArrivalTime(snapshot.getAircraftArrivalTime());
        leg.setDateVariation(snapshot.getDateVariation());
        leg.setDepartureUtcOffset(snapshot.getDepartureUtcOffset());
        leg.setArrivalUtcOffset(snapshot.getArrivalUtcOffset());
        request.getLegs().add(leg);
    }

    private <T> List<T> list(List<T> values) {
        return values == null ? List.of() : values;
    }

    private String formatErrors(List<ScheduleTimeValidationErrorDTO> errors) {
        if (errors == null || errors.isEmpty()) {
            return "unknown schedule time validation error";
        }
        return errors.stream()
                .map(error -> error.getField() + " (" + error.getMessage() + ")")
                .toList()
                .toString();
    }
}
