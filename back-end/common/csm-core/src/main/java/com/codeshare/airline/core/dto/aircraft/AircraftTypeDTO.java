package com.codeshare.airline.core.dto.aircraft;

import com.codeshare.airline.core.enums.common.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AircraftTypeDTO {

    private UUID id;

    private String manufacturer;
    private String modelCode;
    private String icaoCode;
    private String iataCode;

    private String engineType;
    private Integer typicalSeatCapacity;
    private Integer maxRangeKm;
    private Integer maxTakeoffWeightKg;

    private Status statusCode;

    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}