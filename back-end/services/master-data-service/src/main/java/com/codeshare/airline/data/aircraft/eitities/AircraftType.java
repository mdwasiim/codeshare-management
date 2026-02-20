package com.codeshare.airline.data.aircraft.eitities;

import com.codeshare.airline.core.enums.common.Status;
import com.codeshare.airline.persistence.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "MASTER_AIRCRAFT_TYPE",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_AIRCRAFT_ICAO", columnNames = "ICAO_CODE"),
                @UniqueConstraint(name = "UK_AIRCRAFT_MODEL", columnNames = "MODEL_CODE")
        },
        indexes = {
                @Index(name = "IDX_AIRCRAFT_ICAO", columnList = "ICAO_CODE"),
                @Index(name = "IDX_AIRCRAFT_STATUS", columnList = "STATUS_CODE")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class AircraftType extends CSMDataAbstractEntity {

    @Column(name = "MANUFACTURER", nullable = false, length = 100)
    private String manufacturer;

    @Column(name = "MODEL_CODE", nullable = false, length = 100)
    private String modelCode;   // e.g., A320-200

    @Column(name = "ICAO_CODE", nullable = false, length = 4)
    private String icaoCode;    // e.g., A320

    @Column(name = "IATA_CODE", length = 3)
    private String iataCode;    // e.g., 320

    @Column(name = "ENGINE_TYPE", length = 100)
    private String engineType;

    @Column(name = "TYPICAL_SEAT_CAPACITY")
    private Integer typicalSeatCapacity;

    @Column(name = "MAX_RANGE_KM")
    private Integer maxRangeKm;

    @Column(name = "MAX_TAKEOFF_WEIGHT_KG")
    private Integer maxTakeoffWeightKg;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    private Status status;

    @Column(name = "EFFECTIVE_FROM")
    private LocalDate effectiveFrom;

    @Column(name = "EFFECTIVE_TO")
    private LocalDate effectiveTo;

    @PrePersist
    @PreUpdate
    private void normalizeAndValidate() {

        if (manufacturer != null) {
            manufacturer = manufacturer.trim();
        }

        if (modelCode != null) {
            modelCode = modelCode.trim().toUpperCase();
        }

        if (icaoCode != null) {
            icaoCode = icaoCode.trim().toUpperCase();
        }

        if (iataCode != null) {
            iataCode = iataCode.trim().toUpperCase();
        }

        if (effectiveFrom != null && effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {
            throw new IllegalStateException("Invalid effective period.");
        }
    }
}
