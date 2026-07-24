package com.codeshare.airline.platform.core.dto.master.aircraft;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AirlineFleetDTO {

    private Long id;

    private Long airlineId;
    private Long aircraftConfigurationId;

    private Integer aircraftCount;

    private RecordStatus recordStatus;

    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}