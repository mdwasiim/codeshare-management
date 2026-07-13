package com.codeshare.airline.platform.core.dto.codeshare;

import com.codeshare.airline.platform.core.enums.codeshare.CodeshareDisclosureType;
import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CodeshareFlightMappingDTO {

    private Long id;

    private Long routeId;

    private Long agreementId;
    private String operatingFlightNumber;
    private String marketingFlightNumber;

    private Integer marketingFlightStart;
    private Integer marketingFlightEnd;

    private Integer operatingFlightStart;
    private Integer operatingFlightEnd;

    private String flightSuffix;

    private RecordStatus recordStatus;

    private CodeshareDisclosureType disclosureType;

    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;

    // 🔥 Optional read-only fields (response only)
    private String marketingAirlineCode;
    private String operatingAirlineCode;

    private String originAirportCode;
    private String destinationAirportCode;
}