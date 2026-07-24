package com.codeshare.airline.platform.core.dto.master.schedule;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class OperationalSuffixDTO {

    private Long id;
    private String suffixCode;
    private String suffixName;
    private String description;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
