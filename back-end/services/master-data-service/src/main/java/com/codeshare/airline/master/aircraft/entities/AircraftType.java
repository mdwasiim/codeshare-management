package com.codeshare.airline.master.aircraft.entities;

import com.codeshare.airline.platform.core.enums.master.aircraft.AircraftCategory;
import com.codeshare.airline.platform.data.jpa.entity.CSMMasterDataEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "AIRCRAFT_TYPE",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_AIRCRAFT_TYPE_ICAO",
                        columnNames = "ICAO_CODE"
                ),
                @UniqueConstraint(
                        name = "UK_AIRCRAFT_TYPE_IATA",
                        columnNames = "IATA_CODE"
                ),
                @UniqueConstraint(
                        name = "UK_AIRCRAFT_TYPE_MODEL",
                        columnNames = "MODEL"
                )
        },
        indexes = {
                @Index(
                        name = "IDX_AIRCRAFT_TYPE_ICAO",
                        columnList = "ICAO_CODE"
                ),
                @Index(
                        name = "IDX_AIRCRAFT_TYPE_IATA",
                        columnList = "IATA_CODE"
                ),
                @Index(
                        name = "IDX_AIRCRAFT_TYPE_FAMILY",
                        columnList = "AIRCRAFT_FAMILY_ID"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class AircraftType extends CSMMasterDataEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "AIRCRAFT_FAMILY_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_AIRCRAFT_TYPE_FAMILY")
    )
    private AircraftFamily aircraftFamily;

    /**
     * Airbus A320-200
     * Boeing 787-9 Dreamliner
     */
    @NotBlank
    @Column(name = "AIRCRAFT_NAME", nullable = false, length = 150)
    private String aircraftName;

    /**
     * A320-200
     * B737-800
     */
    @NotBlank
    @Column(name = "MODEL", nullable = false, length = 100)
    private String model;

    /**
     * ICAO Aircraft Type Designator
     * Example: A320, B789
     */
    @NotBlank
    @Column(name = "ICAO_CODE", nullable = false, length = 4)
    private String icaoCode;

    /**
     * IATA Equipment Code
     * Example: 320, 789
     */
    @NotBlank
    @Column(name = "IATA_CODE", nullable = false, length = 3)
    private String iataCode;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "CATEGORY", nullable = false, length = 30)
    private AircraftCategory category;

    @Column(name = "TYPICAL_SEAT_CAPACITY")
    private Integer typicalSeatCapacity;

    @Column(name = "MAX_RANGE_KM")
    private Integer maxRangeKm;

    @Column(name = "MAX_TAKEOFF_WEIGHT_KG")
    private Integer maxTakeoffWeightKg;

    @Column(name = "WIDE_BODY", nullable = false)
    private Boolean wideBody = Boolean.FALSE;

    @Column(name = "FREIGHTER", nullable = false)
    private Boolean freighter = Boolean.FALSE;

    @Column(name = "ETOPS_CERTIFIED", nullable = false)
    private Boolean etopsCertified = Boolean.FALSE;

    @Column(name = "IN_PRODUCTION", nullable = false)
    private Boolean inProduction = Boolean.TRUE;

    @Column(name = "DISPLAY_ORDER")
    private Integer displayOrder = 1;

    @PrePersist
    @PreUpdate
    private void normalize() {

        aircraftName = aircraftName.trim();
        model = model.trim().toUpperCase();
        icaoCode = icaoCode.trim().toUpperCase();
        iataCode = iataCode.trim().toUpperCase();
    }
}