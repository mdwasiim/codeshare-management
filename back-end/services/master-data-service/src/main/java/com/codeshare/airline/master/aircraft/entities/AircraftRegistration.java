package com.codeshare.airline.master.aircraft.entities;

import com.codeshare.airline.platform.core.enums.master.aircraft.AircraftRegistrationStatus;
import com.codeshare.airline.platform.data.jpa.entity.CSMMasterDataEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "AIRCRAFT_REGISTRATION",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_AIRCRAFT_REGISTRATION_NUMBER",
                        columnNames = "REGISTRATION_NUMBER"
                )
        },
        indexes = {
                @Index(
                        name = "IDX_AIRCRAFT_REGISTRATION_NUMBER",
                        columnList = "REGISTRATION_NUMBER"
                ),
                @Index(
                        name = "IDX_AIRCRAFT_REGISTRATION_FLEET",
                        columnList = "AIRLINE_FLEET_PROFILE_ID"
                ),
                @Index(
                        name = "IDX_AIRCRAFT_REGISTRATION_STATUS",
                        columnList = "REGISTRATION_STATUS"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class AircraftRegistration extends CSMMasterDataEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "AIRLINE_FLEET_PROFILE_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_REGISTRATION_FLEET_PROFILE")
    )
    private AirlineFleetProfile airlineFleetProfile;

    /**
     * Aircraft Registration
     * Examples:
     * A7-ANB
     * G-ZBKA
     * N781AN
     */
    @NotBlank
    @Column(name = "REGISTRATION_NUMBER", nullable = false, length = 20)
    private String registrationNumber;

    /**
     * Manufacturer Serial Number (MSN)
     */
    @Column(name = "MANUFACTURER_SERIAL_NUMBER", length = 50)
    private String manufacturerSerialNumber;

    /**
     * Production Line Number
     */
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

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "REGISTRATION_STATUS", nullable = false, length = 30)
    private AircraftRegistrationStatus registrationStatus =
            AircraftRegistrationStatus.ACTIVE;

    @Column(name = "DISPLAY_ORDER")
    private Integer displayOrder = 1;

    @PrePersist
    @PreUpdate
    private void normalize() {

        registrationNumber = registrationNumber.trim().toUpperCase();

        if (manufacturerSerialNumber != null) {
            manufacturerSerialNumber =
                    manufacturerSerialNumber.trim().toUpperCase();
        }

        if (lineNumber != null) {
            lineNumber = lineNumber.trim().toUpperCase();
        }
    }
}