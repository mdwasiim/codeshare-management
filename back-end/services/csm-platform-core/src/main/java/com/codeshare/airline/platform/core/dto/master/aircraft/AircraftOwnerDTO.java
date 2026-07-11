package com.codeshare.airline.platform.core.dto.master.aircraft;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.core.enums.master.aircraft.AircraftOwnerType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class AircraftOwnerDTO {
    private UUID id;
    private String ownerCode;
    private String ownerName;
    private AircraftOwnerType ownerType;
    private String iataCode;
    private String icaoCode;
    private UUID countryId;
    private Boolean active;
    private Integer displayOrder;
    private String description;
    private String remarks;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
