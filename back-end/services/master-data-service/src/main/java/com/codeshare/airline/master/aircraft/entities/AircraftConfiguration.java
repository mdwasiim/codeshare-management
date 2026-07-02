package com.codeshare.airline.master.aircraft.entities;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.master.aircraft.entities.enums.ConfigurationSource;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import com.codeshare.airline.master.airline.entities.AirlineCarrier;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "AIRCRAFT_CONFIGURATION",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_AIRCRAFT_CONFIGURATION",
                        columnNames = {
                                "CONFIGURATION_CODE",
                                "AIRLINE_ID"
                        }
                )
        },
        indexes = {
                @Index(name = "IDX_CONFIG_TYPE", columnList = "AIRCRAFT_TYPE_ID"),
                @Index(name = "IDX_CONFIG_AIRLINE", columnList = "AIRLINE_ID"),
                @Index(name = "IDX_CONFIG_STATUS", columnList = "STATUS"),
                @Index(name = "IDX_CONFIG_ACTIVE", columnList = "ACTIVE")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class AircraftConfiguration extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "AIRCRAFT_TYPE_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_CONFIG_AIRCRAFT_TYPE")
    )
    private AircraftType aircraftType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "AIRLINE_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_CONFIG_AIRLINE")
    )
    private AirlineCarrier airline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "AIRCRAFT_OWNER_ID",
            foreignKey = @ForeignKey(name = "FK_CONFIG_OWNER")
    )
    private AircraftOwner aircraftOwner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "CABIN_CREW_EMPLOYER_ID",
            foreignKey = @ForeignKey(name = "FK_CONFIG_CABIN_CREW")
    )
    private CabinCrewOperator cabinCrewEmployer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "COCKPIT_CREW_EMPLOYER_ID",
            foreignKey = @ForeignKey(name = "FK_CONFIG_COCKPIT_CREW")
    )
    private CockpitCrewOperator cockpitCrewEmployer;

    /**
     * ACV Code
     * Example:
     * QR359A
     */
    @Column(name = "CONFIGURATION_CODE", nullable = false, length = 30)
    private String configurationCode;

    @Column(name = "CONFIGURATION_NAME", nullable = false, length = 150)
    private String configurationName;

    @Enumerated(EnumType.STRING)
    @Column(name = "CONFIGURATION_SOURCE", nullable = false, length = 20)
    private ConfigurationSource configurationSource = ConfigurationSource.MANUAL;

    @Column(name = "TOTAL_SEATS", nullable = false)
    private Integer totalSeats;

    @Column(name = "CARGO_CAPACITY_KG")
    private Integer cargoCapacityKg;

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

        if (configurationCode == null || configurationCode.isBlank()) {
            throw new IllegalStateException("Configuration Code is mandatory.");
        }

        if (configurationName == null || configurationName.isBlank()) {
            throw new IllegalStateException("Configuration Name is mandatory.");
        }

        if (aircraftType == null) {
            throw new IllegalStateException("Aircraft Type is mandatory.");
        }

        if (airline == null) {
            throw new IllegalStateException("Airline is mandatory.");
        }

        if (configurationSource == null) {
            throw new IllegalStateException("Configuration Source is mandatory.");
        }

        if (totalSeats == null || totalSeats <= 0) {
            throw new IllegalStateException("Total Seats must be greater than zero.");
        }

        configurationCode = configurationCode.trim().toUpperCase();
        configurationName = configurationName.trim();

        if (effectiveFrom != null &&
                effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {
            throw new IllegalStateException("Effective From cannot be after Effective To.");
        }
    }
}