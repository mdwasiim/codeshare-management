package com.codeshare.airline.core.dto.master.flightcommercial.schedule;

import com.codeshare.airline.core.enums.common.RecordStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class FlightFrequencyDTO {
    private UUID id;
    private String frequencyCode;
    private String frequencyName;
    private Integer operatingDays;
    private Boolean active;
    private Integer displayOrder;
    private String description;
    private String remarks;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
