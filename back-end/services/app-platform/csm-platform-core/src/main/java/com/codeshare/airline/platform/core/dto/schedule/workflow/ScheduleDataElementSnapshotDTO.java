package com.codeshare.airline.platform.core.dto.schedule.workflow;

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
public class ScheduleDataElementSnapshotDTO {
    private Long dataElementId;
    private String code;
    private String value;
    private String scope;
    private Integer sequenceOrder;
    private String boardPoint;
    private String offPoint;
}
