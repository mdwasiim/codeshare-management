package com.codeshare.airline.core.dto.master.messaging;

import com.codeshare.airline.core.enums.common.RecordStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class MessageStatusDTO {

    private UUID id;
    private String messageStatusCode;
    private String messageStatusName;
    private String description;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
