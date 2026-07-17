package com.codeshare.airline.platform.core.dto.master.schedule;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SchedulePriorityDTO {

    private Long id;
    private String priorityCode;
    private String priorityName;
    private Integer priorityLevel;
    private String description;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
