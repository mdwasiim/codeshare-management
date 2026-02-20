package com.codeshare.airline.core.dto.codeshare;

import com.codeshare.airline.core.enums.common.FlightNumberPattern;
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
public class CodeshareDayRuleDTO {

    private UUID id;

    private UUID flightMappingId;

    private FlightNumberPattern flightNumberPattern;

    private String operatingDays;

    private Status statusCode;

    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}