package com.codeshare.airline.core.dto.master.schedule;

import com.codeshare.airline.core.enums.common.RecordStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class ScheduleTypeDTO {

    private UUID id;
    private String messageTypeCode;
    private String messageTypeName;
    private String description;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}