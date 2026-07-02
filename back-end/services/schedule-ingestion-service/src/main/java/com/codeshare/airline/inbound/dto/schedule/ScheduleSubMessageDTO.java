package com.codeshare.airline.inbound.dto.schedule;


import com.codeshare.airline.core.dto.audit.CSMAuditableDTO;
import com.codeshare.airline.inbound.domain.enums.ActionType;
import com.codeshare.airline.inbound.domain.enums.TimeMode;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleSubMessageDTO extends CSMAuditableDTO {

    private ActionType actionType;

    private Integer messageSequenceNumber;

    private String messageReference;

    private TimeMode timeMode;

    /* ================= RAW ================= */

    private String rawMessage;

    @Builder.Default
    private List<String> rawLines = new ArrayList<>();

    /* ================= PROCESSING ================= */

    private String processingStatus;
    private String errorMessage;

    /* ================= PAYLOAD ================= */

    @Builder.Default
    private List<ScheduleFlightDTO> flights = new ArrayList<>();

    public void addFlight(ScheduleFlightDTO flight) {
        if (flight != null) {
            flight.setFlightSequenceNumber(flights.size() + 1);
            flights.add(flight);
        }
    }
}