package com.codeshare.airline.platform.core.dto.master.aircraft;

import com.codeshare.airline.platform.core.enums.common.CabinClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AircraftCabinLayoutDTO {

    private Long id;

    private Long aircraftConfigurationId;

    private CabinClass cabinClass;

    private Integer seatCount;
    private Integer seatPitchInch;
    private Integer seatWidthInch;
}