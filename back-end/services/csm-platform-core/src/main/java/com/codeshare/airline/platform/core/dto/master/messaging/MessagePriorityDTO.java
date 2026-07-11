package com.codeshare.airline.platform.core.dto.master.messaging;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class MessagePriorityDTO {

    private UUID id;
    private String priorityCode;
    private String priorityName;
    private Integer priorityLevel;
    private String description;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
