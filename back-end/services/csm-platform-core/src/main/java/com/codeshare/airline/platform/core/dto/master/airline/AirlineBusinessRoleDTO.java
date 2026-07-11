package com.codeshare.airline.platform.core.dto.master.airline;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.core.enums.master.airline.AirlineRoleCategory;
import com.codeshare.airline.platform.core.enums.master.airline.AirlineRoleScope;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class AirlineBusinessRoleDTO {
    private UUID id;
    private UUID airlineId;
    private String roleCode;
    private String roleName;
    private AirlineRoleScope roleScope;
    private AirlineRoleCategory roleCategory;
    private Boolean active;
    private Integer displayOrder;
    private String description;
    private String remarks;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
