package com.codeshare.airline.platform.core.dto.master.aircraft;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
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

    private RecordStatus recordStatus;

    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}