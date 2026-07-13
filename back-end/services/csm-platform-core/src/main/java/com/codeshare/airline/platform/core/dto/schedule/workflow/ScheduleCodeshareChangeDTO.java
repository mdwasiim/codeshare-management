package com.codeshare.airline.platform.core.dto.schedule.workflow;

import com.codeshare.airline.platform.core.enums.schedule.ScheduleCodeshareChangeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleCodeshareChangeDTO {
    private UUID changeId;
    private UUID liveCodeshareId;
    private ScheduleCodeshareChangeType changeType;
    private ScheduleCodeshareSnapshotDTO oldValue;
    private ScheduleCodeshareSnapshotDTO newValue;
}
