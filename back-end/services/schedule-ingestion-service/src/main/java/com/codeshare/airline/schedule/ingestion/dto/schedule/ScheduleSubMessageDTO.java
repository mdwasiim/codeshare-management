package com.codeshare.airline.schedule.ingestion.dto.schedule;


import com.codeshare.airline.core.dto.audit.CSMAuditableDTO;
import com.codeshare.airline.schedule.ingestion.domain.enums.ActionType;
import com.codeshare.airline.schedule.ingestion.domain.enums.TimeMode;
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

    private String rawActionLine;

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

    @Builder.Default
    private List<String> supplementaryInfo = new ArrayList<>();

    public void addFlight(ScheduleFlightDTO flight) {
        if (flight != null) {
            flight.setFlightSequenceNumber(flights.size() + 1);
            flights.add(flight);
        }
    }

    public void addSupplementaryInfo(String line) {
        if (line != null && !line.isBlank()) {
            supplementaryInfo.add(line);
        }
    }
}
