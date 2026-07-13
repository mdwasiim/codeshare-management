package com.codeshare.airline.platform.core.dto.schedule.workflow;

import com.codeshare.airline.platform.core.enums.schedule.ScheduleSegmentChangeType;
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
public class ScheduleSegmentChangeDTO {
    private Long segmentChangeId;
    private Long liveSegmentId;
    private ScheduleSegmentChangeType changeType;
    private ScheduleSegmentSnapshotDTO oldValue;
    private ScheduleSegmentSnapshotDTO newValue;

    @Builder.Default
    private List<ScheduleDataElementChangeDTO> dataElementChanges = new ArrayList<>();
}
