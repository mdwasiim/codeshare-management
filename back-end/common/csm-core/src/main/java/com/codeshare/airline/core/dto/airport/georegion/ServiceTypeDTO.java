package com.codeshare.airline.core.dto.airport.georegion;

import com.codeshare.airline.core.enums.common.RecordStatus;
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
    private String description;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}