package com.codeshare.airline.platform.core.dto.schedule.workflow;

import com.codeshare.airline.platform.core.enums.schedule.ScheduleSegmentChangeType;
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
public class ScheduleSegmentChangeDTO {
    private UUID segmentChangeId;
    private UUID liveSegmentId;
    private ScheduleSegmentChangeType changeType;
    private ScheduleSegmentSnapshotDTO oldValue;
    private ScheduleSegmentSnapshotDTO newValue;

    @Builder.Default
    private List<ScheduleDataElementChangeDTO> dataElementChanges = new ArrayList<>();
}
