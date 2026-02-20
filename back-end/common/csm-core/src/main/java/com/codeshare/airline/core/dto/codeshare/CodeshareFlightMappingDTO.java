package com.codeshare.airline.core.dto.codeshare;

import com.codeshare.airline.core.enums.codeshare.CodeshareDisclosureType;
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
public class CodeshareFlightMappingDTO {

    private UUID id;

    private UUID routeId;

    private UUID agreementId;
    private String operatingFlightNumber;
    private String marketingFlightNumber;

    private Integer marketingFlightStart;
    private Integer marketingFlightEnd;

    private Integer operatingFlightStart;
    private Integer operatingFlightEnd;

    private String flightSuffix;

    private Status statusCode;

    private CodeshareDisclosureType disclosureType;

    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;

    // 🔥 Optional read-only fields (response only)
    private String marketingAirlineCode;
    private String operatingAirlineCode;

    private String originAirportCode;
    private String destinationAirportCode;
}