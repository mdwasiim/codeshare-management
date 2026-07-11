package com.codeshare.airline.master.aircraft.entities;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import com.codeshare.airline.platform.core.enums.master.aircraft.AircraftRegistrationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import com.codeshare.airline.master.airlines.entities.AirlineCarrier;

@Entity
@Table(
        name = "AIRCRAFT_REGISTRATION",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_AIRCRAFT_REGISTRATION",
                        columnNames = "REGISTRATION_NUMBER"
                )
        },
        indexes = {
                @Index(
                        name = "IDX_AIRCRAFT_REGISTRATION_STATUS",
                        columnList = "STATUS"
                ),
                @Index(
                        name = "IDX_AIRCRAFT_REGISTRATION_OPERATOR",
                        columnList = "OPERATOR_AIRLINE_ID"
                ),
                @Index(
                        name = "IDX_AIRCRAFT_REGISTRATION_CONFIGURATION",
                        columnList = "AIRCRAFT_CONFIGURATION_ID"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class AircraftRegistration extends CSMDataAbstractEntity {

    @Column(name = "REGISTRATION_NUMBER", nullable = false, length = 20)
    private String registrationNumber;

    @Column(name = "REGISTRATION_NAME", nullable = false, length = 150)
    private String registrationName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "AIRCRAFT_TYPE_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_REGISTRATION_TYPE")
    )
    private AircraftType aircraftType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "AIRCRAFT_CONFIGURATION_ID",
            foreignKey = @ForeignKey(name = "FK_REGISTRATION_CONFIGURATION")
    )
    private AircraftConfiguration aircraftConfiguration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "AIRCRAFT_OWNER_ID",
            foreignKey = @ForeignKey(name = "FK_REGISTRATION_OWNER")
    )
    private AircraftOwner aircraftOwner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "OPERATOR_AIRLINE_ID",
            foreignKey = @ForeignKey(name = "FK_REGISTRATION_OPERATOR")
    )
    private AirlineCarrier operatorAirline;

    @Column(name = "MANUFACTURER_SERIAL_NUMBER", length = 50)
    private String manufacturerSerialNumber;

    @Column(name = "LINE_NUMBER", length = 20)
    private String lineNumber;

    @Column(name = "MANUFACTURE_DATE")
    private LocalDate manufactureDate;

    @Column(name = "DELIVERY_DATE")
    private LocalDate deliveryDate;

    @Column(name = "RETIREMENT_DATE")
    private LocalDate retirementDate;

    @Column(name = "LEASE_EXPIRY_DATE")
    private LocalDate leaseExpiryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "REGISTRATION_STATUS", nullable = false, length = 30)
    private AircraftRegistrationStatus registrationStatus =
            AircraftRegistrationStatus.ACTIVE;

    @Column(name = "ACTIVE", nullable = false)
    private Boolean active = Boolean.TRUE;

    @Column(name = "DISPLAY_ORDER")
    private Integer displayOrder = 1;

    @Column(name = "DESCRIPTION", length = 500)
    private String description;

    @Column(name = "REMARKS", length = 1000)
    private String remarks;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    private RecordStatus recordStatus = RecordStatus.ACTIVE;

    @Column(name = "EFFECTIVE_FROM")
    private LocalDate effectiveFrom;

    @Column(name = "EFFECTIVE_TO")
    private LocalDate effectiveTo;

    @PrePersist
    @PreUpdate
    private void normalizeAndValidate() {

        if (registrationNumber == null || registrationNumber.isBlank()) {
            throw new IllegalStateException("Registration Number is mandatory.");
        }

        if (registrationName == null || registrationName.isBlank()) {
            throw new IllegalStateException("Registration Name is mandatory.");
        }

        if (aircraftType == null) {
            throw new IllegalStateException("Aircraft Type is mandatory.");
        }

        registrationNumber = registrationNumber.trim().toUpperCase();
        registrationName = registrationName.trim();

        if (manufacturerSerialNumber != null) {
            manufacturerSerialNumber =
                    manufacturerSerialNumber.trim().toUpperCase();
        }

        if (lineNumber != null) {
            lineNumber = lineNumber.trim().toUpperCase();
        }

        if (effectiveFrom != null &&
                effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {
            throw new IllegalStateException(
                    "Effective From cannot be after Effective To."
            );
        }
    }
}
