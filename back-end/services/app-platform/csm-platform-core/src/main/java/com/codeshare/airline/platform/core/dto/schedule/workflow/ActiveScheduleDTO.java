package com.codeshare.airline.platform.core.dto.schedule.workflow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActiveScheduleDTO {
    private String airlineCode;
    private Instant asOf;

    @Builder.Default
    private List<ScheduleFlightSnapshotDTO> flights = new ArrayList<>();
}

