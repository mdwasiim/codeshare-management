package com.codeshare.airline.platform.core.dto.codeshare;

import com.codeshare.airline.platform.core.enums.common.FlightNumberPattern;
import com.codeshare.airline.platform.core.enums.common.RecordStatus;
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

    private RecordStatus recordStatus;

    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}