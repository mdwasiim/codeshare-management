package com.codeshare.airline.core.dto.aircraft;

import com.codeshare.airline.core.enums.common.CabinClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AircraftCabinLayoutDTO {

    private UUID id;

    private UUID aircraftConfigurationId;

    private CabinClass cabinClass;

    private Integer seatCount;
    private Integer seatPitchInch;
    private Integer seatWidthInch;
}