package com.codeshare.airline.platform.core.dto.master.schedule;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ScheduleTypeDTO {

    private Long id;
    private String messageTypeCode;
    private String messageTypeName;
    private String description;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}