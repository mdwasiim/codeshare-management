package com.codeshare.airline.platform.core.dto.master.aircraft;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.core.enums.master.aircraft.AircraftRegistrationStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AircraftRegistrationDTO {
    private Long id;
    private String registrationNumber;
    private String registrationName;
    private Long aircraftTypeId;
    private Long aircraftConfigurationId;
    private Long aircraftOwnerId;
    private Long operatorAirlineId;
    private String manufacturerSerialNumber;
    private String lineNumber;
    private LocalDate manufactureDate;
    private LocalDate deliveryDate;
    private LocalDate retirementDate;
    private LocalDate leaseExpiryDate;
    private AircraftRegistrationStatus registrationStatus;
    private Boolean active;
    private Integer displayOrder;
    private String description;
    private String remarks;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
