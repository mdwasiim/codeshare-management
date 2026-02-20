package com.codeshare.airline.data.aircraft.eitities;

import com.codeshare.airline.core.enums.common.CabinClass;
import com.codeshare.airline.persistence.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "MASTER_AIRCRAFT_CABIN_LAYOUT",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_CONFIG_CABIN",
                        columnNames = {"AIRCRAFT_CONFIG_ID", "CABIN_CLASS"}
                )
        },
        indexes = {
                @Index(name = "IDX_CABIN_CONFIG", columnList = "AIRCRAFT_CONFIG_ID")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class AircraftCabinLayout extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "AIRCRAFT_CONFIG_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_CABIN_CONFIG")
    )
    private AircraftConfiguration aircraftConfiguration;

    @Enumerated(EnumType.STRING)
    @Column(name = "CABIN_CLASS", nullable = false, length = 20)
    private CabinClass cabinClass;
    // FIRST / BUSINESS / PREMIUM_ECONOMY / ECONOMY

    @Column(name = "SEAT_COUNT", nullable = false)
    private Integer seatCount;

    @Column(name = "SEAT_PITCH_INCH")
    private Integer seatPitchInch;

    @Column(name = "SEAT_WIDTH_INCH")
    private Integer seatWidthInch;

    @PrePersist
    @PreUpdate
    private void normalizeAndValidate() {

        if (seatCount == null || seatCount <= 0) {
            throw new IllegalStateException("Seat count must be greater than zero.");
        }

        if (seatPitchInch != null && seatPitchInch <= 0) {
            throw new IllegalStateException("Seat pitch must be positive.");
        }

        if (seatWidthInch != null && seatWidthInch <= 0) {
            throw new IllegalStateException("Seat width must be positive.");
        }
    }
}