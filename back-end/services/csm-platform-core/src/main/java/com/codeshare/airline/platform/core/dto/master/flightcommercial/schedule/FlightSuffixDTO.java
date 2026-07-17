package com.codeshare.airline.platform.core.dto.master.flightcommercial.schedule;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class FlightSuffixDTO {
    private Long id;
    private String suffixCode;
    private String suffixName;
    private String suffixMeaning;
    private Boolean active;
    private Integer displayOrder;
    private String description;
    private String remarks;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
