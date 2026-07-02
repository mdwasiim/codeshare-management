package com.codeshare.airline.core.dto.aircraft;

import com.codeshare.airline.core.enums.common.RecordStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class AircraftFamilyDTO {
    private UUID id;
    private String familyCode;
    private String familyName;
    private UUID manufacturerId;
    private String description;
    private Integer displayOrder;
    private Boolean active;
    private RecordStatus recordStatus;
    private String remarks;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
