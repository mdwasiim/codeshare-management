package com.codeshare.airline.platform.core.dto.master.schedule;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class ScheduleCategoryDTO {

    private UUID id;
    private String categoryCode;
    private String categoryName;
    private String description;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
