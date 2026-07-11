package com.codeshare.airline.master.aircraft.entities;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.core.enums.master.aircraft.AircraftCategory;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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
                        name = "IDX_AIRCRAFT_TYPE_STATUS",
                        columnList = "STATUS"
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
public class AircraftType extends CSMDataAbstractEntity {

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
    @Column(name = "AIRCRAFT_NAME", nullable = false, length = 150)
    private String aircraftName;

    /**
     * A320-200
     * B737-800
     * A350-900
     */
    @Column(name = "MODEL", nullable = false, length = 100)
    private String model;

    /**
     * ICAO Aircraft Type Designator
     * Example:
     * A320
     * B789
     */
    @Column(name = "ICAO_CODE", nullable = false, length = 4)
    private String icaoCode;

    /**
     * IATA Aircraft Type
     * Example:
     * 320
     * 789
     */
    @Column(name = "IATA_CODE", nullable = false, length = 3)
    private String iataCode;

    /**
     * CFM56
     * LEAP-1A
     * Trent XWB
     */
    @Column(name = "ENGINE_TYPE", length = 100)
    private String engineType;

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

    @Column(name = "ACTIVE", nullable = false)
    private Boolean active = Boolean.TRUE;

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

        if (aircraftFamily == null) {
            throw new IllegalStateException("Aircraft Family is mandatory.");
        }

        if (aircraftName == null || aircraftName.isBlank()) {
            throw new IllegalStateException("Aircraft Name is mandatory.");
        }

        if (model == null || model.isBlank()) {
            throw new IllegalStateException("Model is mandatory.");
        }

        if (icaoCode == null || icaoCode.isBlank()) {
            throw new IllegalStateException("ICAO Code is mandatory.");
        }

        if (iataCode == null || iataCode.isBlank()) {
            throw new IllegalStateException("IATA Code is mandatory.");
        }

        aircraftName = aircraftName.trim();

        model = model.trim().toUpperCase();

        icaoCode = icaoCode.trim().toUpperCase();

        iataCode = iataCode.trim().toUpperCase();

        if (effectiveFrom != null &&
                effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {

            throw new IllegalStateException(
                    "Effective From cannot be after Effective To."
            );
        }
    }
}