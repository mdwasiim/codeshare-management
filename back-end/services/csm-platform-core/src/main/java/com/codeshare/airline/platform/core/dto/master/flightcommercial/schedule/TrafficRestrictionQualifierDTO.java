package com.codeshare.airline.platform.core.dto.master.flightcommercial.schedule;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class TrafficRestrictionQualifierDTO {
    private UUID id;
    private UUID trafficRestrictionCodeId;
    private String qualifierCode;
    private String qualifierName;
    private String iataDefinition;
    private Boolean active;
    private Integer displayOrder;
    private String description;
    private String remarks;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
