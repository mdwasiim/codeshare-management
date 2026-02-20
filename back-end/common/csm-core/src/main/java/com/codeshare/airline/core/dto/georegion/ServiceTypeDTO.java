package com.codeshare.airline.core.dto.georegion;

import com.codeshare.airline.core.enums.common.Status;
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
    private Status statusCode;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}