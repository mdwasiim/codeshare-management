package com.codeshare.airline.core.dto.airport.georegion;

import com.codeshare.airline.core.enums.common.RecordStatus;
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
public class AirlineCarrierDTO {

    private UUID id;

    private String legalName;
    private String commercialName;

    private String iataCode;
    private String icaoCode;
    private String callsign;

    private UUID countryId;

    private RecordStatus recordStatus;

    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}