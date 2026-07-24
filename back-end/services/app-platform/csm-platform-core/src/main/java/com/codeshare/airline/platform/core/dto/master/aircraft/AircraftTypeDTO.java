package com.codeshare.airline.platform.core.dto.master.aircraft;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AircraftTypeDTO {

    private Long id;

    private String manufacturer;
    private String modelCode;
    private String icaoCode;
    private String iataCode;

    private String engineType;
    private Integer typicalSeatCapacity;
    private Integer maxRangeKm;
    private Integer maxTakeoffWeightKg;

    private Boolean active;
    private RecordStatus recordStatus;

    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
