package com.codeshare.airline.platform.core.dto.schedule.workflow;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleFlightChangeDTO {
    private UUID flightChangeId;
    private String airlineCode;
    private String flightNumber;
    private String operationalSuffix;
    private String itineraryVariationId;

    @Builder.Default
    private List<ScheduleLegChangeDTO> legChanges = new ArrayList<>();
}
