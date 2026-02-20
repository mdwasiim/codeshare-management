package com.codeshare.airline.core.dto.aircraft;

import com.codeshare.airline.core.enums.common.Status;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AirlineFleetDTO {

    private UUID id;

    private UUID airlineId;
    private UUID aircraftConfigurationId;

    private Integer aircraftCount;

    private Status statusCode;

    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}