package com.codeshare.airline.master.aircraft.entities;

import com.codeshare.airline.master.airlines.entities.Airline;
import com.codeshare.airline.platform.core.enums.master.aircraft.FleetStatus;
import com.codeshare.airline.platform.data.jpa.entity.CSMMasterDataEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "AIRLINE_FLEET_PROFILE",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_AIRLINE_FLEET_PROFILE",
                        columnNames = {
                                "AIRLINE_ID",
                                "AIRCRAFT_TYPE_ID",
                                "AIRCRAFT_CONFIGURATION_ID"
                        }
                )
        },
        indexes = {
                @Index(
                        name = "IDX_FLEET_PROFILE_AIRLINE",
                        columnList = "AIRLINE_ID"
                ),
                @Index(
                        name = "IDX_FLEET_PROFILE_TYPE",
                        columnList = "AIRCRAFT_TYPE_ID"
                ),
                @Index(
                        name = "IDX_FLEET_PROFILE_CONFIGURATION",
                        columnList = "AIRCRAFT_CONFIGURATION_ID"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class AirlineFleetProfile extends CSMMasterDataEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "AIRLINE_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_FLEET_PROFILE_AIRLINE")
    )
    private Airline airline;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "AIRCRAFT_TYPE_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_FLEET_PROFILE_AIRCRAFT_TYPE")
    )
    private AircraftType aircraftType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "AIRCRAFT_CONFIGURATION_ID",
            foreignKey = @ForeignKey(name = "FK_FLEET_PROFILE_CONFIGURATION")
    )
    private AircraftConfiguration aircraftConfiguration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "AIRCRAFT_OWNER_ID",
            foreignKey = @ForeignKey(name = "FK_FLEET_PROFILE_OWNER")
    )
    private AircraftOwner aircraftOwner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "CABIN_CREW_EMPLOYER_ID",
            foreignKey = @ForeignKey(name = "FK_FLEET_PROFILE_CABIN_CREW")
    )
    private CabinCrewEmployer cabinCrewEmployer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "COCKPIT_CREW_EMPLOYER_ID",
            foreignKey = @ForeignKey(name = "FK_FLEET_PROFILE_COCKPIT_CREW")
    )
    private CockpitCrewEmployer cockpitCrewEmployer;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "FLEET_STATUS", nullable = false, length = 30)
    private FleetStatus fleetStatus = FleetStatus.ACTIVE;

    @Column(name = "DEFAULT_CONFIGURATION", nullable = false)
    private Boolean defaultConfiguration = Boolean.FALSE;

    @Column(name = "DISPLAY_ORDER")
    private Integer displayOrder = 1;
}