package com.codeshare.airline.platform.core.dto.master.messaging;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DataElementIdentifierDTO {

    private Long id;
    private String deiCode;
    private String deiName;
    private String deiScope;
    private String description;
    private Long standardMessageIdentifierId;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
