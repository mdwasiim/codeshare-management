package com.codeshare.airline.core.dto.ssim;

import com.codeshare.airline.core.enums.common.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class MessageTypeDTO {

    private UUID id;
    private String messageTypeCode;
    private String messageTypeName;
    private String description;
    private Status statusCode;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}