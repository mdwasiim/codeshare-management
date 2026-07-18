package com.codeshare.airline.platform.core.dto.master.validation;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class ScheduleTimeValidationLegDTO {

    private String departureAirport;
    private String arrivalAirport;
    private LocalDate departureDate;
    private LocalDate arrivalDate;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private String dateVariation;
    private String departureUtcOffset;
    private String arrivalUtcOffset;
}
