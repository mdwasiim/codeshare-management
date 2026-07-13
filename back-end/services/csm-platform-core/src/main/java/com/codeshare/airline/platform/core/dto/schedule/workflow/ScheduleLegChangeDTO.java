package com.codeshare.airline.platform.core.dto.schedule.workflow;

import com.codeshare.airline.platform.core.enums.schedule.ScheduleLegChangeType;
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
public class ScheduleLegChangeDTO {
    private Long legChangeId;
    private Long liveLegId;
    private ScheduleLegChangeType changeType;
    private ScheduleLegSnapshotDTO oldValue;
    private ScheduleLegSnapshotDTO newValue;

    @Builder.Default
    private List<ScheduleSegmentChangeDTO> segmentChanges = new ArrayList<>();

    @Builder.Default
    private List<ScheduleDataElementChangeDTO> legDataElementChanges = new ArrayList<>();

    @Builder.Default
    private List<ScheduleCodeshareChangeDTO> codeshareChanges = new ArrayList<>();
}
