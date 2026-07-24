package com.codeshare.airline.platform.core.dto.schedule.workflow;

import com.codeshare.airline.platform.core.enums.schedule.ScheduleDataElementChangeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleDataElementChangeDTO {
    private Long changeId;
    private Long liveDataElementId;
    private ScheduleDataElementChangeType changeType;
    private String scope;
    private String code;
    private Integer sequenceOrder;
    private String boardPoint;
    private String offPoint;
    private String oldValue;
    private String newValue;
}
