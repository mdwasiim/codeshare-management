package com.codeshare.airline.core.dto.georegion;

import com.codeshare.airline.core.enums.common.Status;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AirportDTO {

    private UUID id;

    private String iataCode;
    private String icaoCode;
    private String airportName;

    private UUID cityId;
    private UUID countryId;
    private UUID timezoneId;

    private BigDecimal latitude;
    private BigDecimal longitude;

    private Integer elevationFeet;

    private Boolean international;
    private Boolean hub;

    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;

    private Status statusCode;
}