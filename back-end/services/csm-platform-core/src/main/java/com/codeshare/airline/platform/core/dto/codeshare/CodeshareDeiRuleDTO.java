package com.codeshare.airline.platform.core.dto.codeshare;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CodeshareDeiRuleDTO {

    private UUID id;

    private UUID flightMappingId;
    private UUID deiId;

    private String deiValue;

    private Boolean mandatory;
    private Boolean overrideExisting;

    private RecordStatus recordStatus;

    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;

    private Integer priority;
}