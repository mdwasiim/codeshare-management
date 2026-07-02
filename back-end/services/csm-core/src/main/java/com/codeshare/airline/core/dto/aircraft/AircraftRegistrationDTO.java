package com.codeshare.airline.core.dto.aircraft;

import com.codeshare.airline.core.enums.common.RecordStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class AircraftRegistrationDTO {
    private UUID id;
    private String registrationNumber;
    private String registrationName;
    private UUID aircraftTypeId;
    private UUID aircraftConfigurationId;
    private UUID aircraftOwnerId;
    private UUID operatorAirlineId;
    private String manufacturerSerialNumber;
    private String lineNumber;
    private LocalDate manufactureDate;
    private LocalDate deliveryDate;
    private LocalDate retirementDate;
    private LocalDate leaseExpiryDate;
    private String registrationStatus;
    private Boolean active;
    private Integer displayOrder;
    private String description;
    private String remarks;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
