package com.codeshare.airline.core.dto.georegion;

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
public class AirlineCarrierDTO {

    private UUID id;

    private String legalName;
    private String commercialName;

    private String iataCode;
    private String icaoCode;
    private String callsign;

    private UUID countryId;

    private Status status;

    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}