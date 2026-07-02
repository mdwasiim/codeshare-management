package com.codeshare.airline.core.dto.master.airline;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.core.enums.master.airline.AirlineAliasType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class AirlineAliasDTO {
    private UUID id;
    private UUID airlineId;
    private String aliasCode;
    private String aliasName;
    private AirlineAliasType aliasType;
    private Boolean active;
    private Integer displayOrder;
    private String description;
    private String remarks;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
