package com.codeshare.airline.platform.core.dto.master.messaging;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class ActionCodeDTO {

    private UUID id;
    private String actionCode;
    private String actionName;
    private String description;
    private UUID actionIdentifierId;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
