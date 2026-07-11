package com.codeshare.airline.platform.core.dto.master.flightcommercial.passenger;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class ServiceTypeDTO {
    private UUID id;
    private String serviceTypeCode;
    private String serviceTypeName;
    private String category;
    private String iataDefinition;
    private Boolean active;
    private Integer displayOrder;
    private String description;
    private String remarks;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
