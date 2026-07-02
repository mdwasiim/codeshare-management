package com.codeshare.airline.core.dto.ssim;

import com.codeshare.airline.core.enums.common.RecordStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class StandardMessageIdentifierDTO {

    private UUID id;
    private String messageIdentifier;
    private String messageIdentifierName;
    private String description;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
