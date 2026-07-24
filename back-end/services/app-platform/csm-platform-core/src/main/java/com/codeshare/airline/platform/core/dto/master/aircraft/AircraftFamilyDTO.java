package com.codeshare.airline.platform.core.dto.master.aircraft;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AircraftFamilyDTO {
    private Long id;
    private String familyCode;
    private String familyName;
    private Long manufacturerId;
    private String description;
    private Integer displayOrder;
    private Boolean active;
    private RecordStatus recordStatus;
    private String remarks;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
