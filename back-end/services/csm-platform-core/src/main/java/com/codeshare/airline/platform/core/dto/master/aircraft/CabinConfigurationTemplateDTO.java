package com.codeshare.airline.platform.core.dto.master.aircraft;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.core.enums.master.aircraft.CabinConfigurationType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class CabinConfigurationTemplateDTO {
    private UUID id;
    private String configurationCode;
    private String configurationName;
    private CabinConfigurationType configurationType;
    private Boolean active;
    private Integer displayOrder;
    private String description;
    private String remarks;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
