package com.codeshare.airline.core.dto.master.airline;

import com.codeshare.airline.core.enums.common.RecordStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class AirlineCarrierDTO {
    private UUID id;
    private String iataCode;
    private String icaoCode;
    private String iataNumericCode;
    private String legalName;
    private String commercialName;
    private String displayName;
    private String callsign;
    private UUID countryId;
    private UUID headquartersCityId;
    private UUID homeAirportId;
    private UUID allianceId;
    private String website;
    private String email;
    private String phone;
    private Boolean active;
    private Integer displayOrder;
    private String description;
    private String remarks;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
