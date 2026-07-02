package com.codeshare.airline.core.dto.aircraft;

import com.codeshare.airline.core.enums.common.RecordStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class AircraftManufacturerDTO {
    private UUID id;
    private String manufacturerCode;
    private String manufacturerName;
    private String shortName;
    private UUID countryId;
    private String website;
    private Boolean active;
    private Integer displayOrder;
    private String description;
    private String remarks;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
