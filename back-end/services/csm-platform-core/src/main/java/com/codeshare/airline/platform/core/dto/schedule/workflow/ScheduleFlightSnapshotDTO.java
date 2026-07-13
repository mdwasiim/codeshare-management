package com.codeshare.airline.platform.core.dto.schedule.workflow;

import com.codeshare.airline.platform.core.enums.schedule.ScheduleInstructionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleFlightSnapshotDTO {
    private Long flightId;
    private String airlineCode;
    private String flightNumber;
    private String operationalSuffix;
    private String itineraryVariationId;
    private String flightStatus;
    private ScheduleInstructionType instructionType;
    private String messageReference;
    private Integer flightSequenceNumber;

    @Builder.Default
    private List<String> supplementaryInfo = new ArrayList<>();

    @Builder.Default
    private List<ScheduleDataElementSnapshotDTO> flightDataElements = new ArrayList<>();

    @Builder.Default
    private List<ScheduleLegSnapshotDTO> legs = new ArrayList<>();
}
