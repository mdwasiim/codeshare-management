package com.codeshare.airline.master.aircraft.entities;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import com.codeshare.airline.master.aircraft.entities.enums.FleetStatus;
import com.codeshare.airline.master.georegion.eitities.AirlineCarrier;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "AIRLINE_FLEET",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_AIRLINE_FLEET",
                        columnNames = {
                                "AIRLINE_ID",
                                "AIRCRAFT_TYPE_ID",
                                "AIRCRAFT_CONFIG_ID"
                        }
                )
        },
        indexes = {
                @Index(name = "IDX_FLEET_AIRLINE", columnList = "AIRLINE_ID"),
                @Index(name = "IDX_FLEET_TYPE", columnList = "AIRCRAFT_TYPE_ID"),
                @Index(name = "IDX_FLEET_CONFIGURATION", columnList = "AIRCRAFT_CONFIG_ID"),
                @Index(name = "IDX_FLEET_STATUS", columnList = "STATUS")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class AirlineFleetProfile extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "AIRLINE_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_FLEET_AIRLINE")
    )
    private AirlineCarrier airline;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "AIRCRAFT_TYPE_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_FLEET_AIRCRAFT_TYPE")
    )
    private AircraftType aircraftType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "AIRCRAFT_CONFIG_ID",
            foreignKey = @ForeignKey(name = "FK_FLEET_CONFIGURATION")
    )
    private AircraftConfiguration aircraftConfiguration;

    @Column(name = "PLANNED_AIRCRAFT_COUNT", nullable = false)
    private Integer plannedAircraftCount = 0;

    @Column(name = "ACTIVE_AIRCRAFT_COUNT", nullable = false)
    private Integer activeAircraftCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "FLEET_STATUS", nullable = false, length = 30)
    private FleetStatus fleetStatus = FleetStatus.ACTIVE;

    @Column(name = "DEFAULT_CONFIGURATION")
    private Boolean defaultConfiguration = Boolean.FALSE;

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

        if (airline == null) {
            throw new IllegalStateException("Airline is mandatory.");
        }

        if (aircraftType == null) {
            throw new IllegalStateException("Aircraft Type is mandatory.");
        }

        if (plannedAircraftCount < 0) {
            throw new IllegalStateException("Planned aircraft count cannot be negative.");
        }

        if (activeAircraftCount < 0) {
            throw new IllegalStateException("Active aircraft count cannot be negative.");
        }

        if (activeAircraftCount > plannedAircraftCount) {
            throw new IllegalStateException("Active aircraft count cannot exceed planned aircraft count.");
        }

        if (effectiveFrom != null &&
                effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {
            throw new IllegalStateException("Effective From cannot be after Effective To.");
        }
    }
}
