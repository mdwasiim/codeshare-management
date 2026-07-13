package com.codeshare.airline.platform.core.dto.master.georegion;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AirportDTO {

    private Long id;

    private String iataCode;
    private String icaoCode;
    private String airportName;

    private Long cityId;
    private Long countryId;
    private Long timezoneId;

    private BigDecimal latitude;
    private BigDecimal longitude;

    private Integer elevationFeet;

    private Boolean international;
    private Boolean hub;

    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;

    private RecordStatus recordStatus;
}