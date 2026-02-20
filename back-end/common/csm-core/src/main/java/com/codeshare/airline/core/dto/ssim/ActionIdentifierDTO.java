package com.codeshare.airline.core.dto.ssim;

import com.codeshare.airline.core.enums.common.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class ActionIdentifierDTO {

    private UUID id;
    private String actionCode;
    private String actionName;
    private String description;
    private String applicableMessageType;
    private Status statusCode;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}