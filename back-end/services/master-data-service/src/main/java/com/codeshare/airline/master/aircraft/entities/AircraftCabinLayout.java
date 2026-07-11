package com.codeshare.airline.master.aircraft.entities;

import com.codeshare.airline.platform.core.enums.common.CabinClass;
import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "AIRCRAFT_CABIN_LAYOUT",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_AIRCRAFT_CONFIGURATION_CABIN",
                        columnNames = {
                                "AIRCRAFT_CONFIG_ID",
                                "CABIN_CLASS"
                        }
                )
        },
        indexes = {
                @Index(
                        name = "IDX_AIRCRAFT_CABIN_CONFIGURATION",
                        columnList = "AIRCRAFT_CONFIG_ID"
                ),
                @Index(
                        name = "IDX_AIRCRAFT_CABIN_CLASS",
                        columnList = "CABIN_CLASS"
                )
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
            foreignKey = @ForeignKey(name = "FK_AIRCRAFT_CABIN_CONFIGURATION")
    )
    private AircraftConfiguration aircraftConfiguration;

    @Enumerated(EnumType.STRING)
    @Column(name = "CABIN_CLASS", nullable = false, length = 30)
    private CabinClass cabinClass;

    /**
     * F
     * C
     * W
     * Y
     */
    @Column(name = "CABIN_CODE", length = 5)
    private String cabinCode;

    @Column(name = "SEAT_COUNT", nullable = false)
    private Integer seatCount;

    @Column(name = "SEAT_ROWS")
    private Integer seatRows;

    @Column(name = "SEATS_PER_ROW")
    private Integer seatsPerRow;

    @Column(name = "CREW_SEATS")
    private Integer crewSeats;

    @Column(name = "SEAT_PITCH_INCH")
    private Integer seatPitchInch;

    @Column(name = "SEAT_WIDTH_INCH")
    private Integer seatWidthInch;

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

    @PrePersist
    @PreUpdate
    private void normalizeAndValidate() {

        if (aircraftConfiguration == null) {
            throw new IllegalStateException("Aircraft Configuration is mandatory.");
        }

        if (cabinClass == null) {
            throw new IllegalStateException("Cabin Class is mandatory.");
        }

        if (seatCount == null || seatCount <= 0) {
            throw new IllegalStateException("Seat Count must be greater than zero.");
        }

        if (cabinCode != null) {
            cabinCode = cabinCode.trim().toUpperCase();
        }

        if (seatRows != null && seatRows <= 0) {
            throw new IllegalStateException("Seat Rows must be greater than zero.");
        }

        if (seatsPerRow != null && seatsPerRow <= 0) {
            throw new IllegalStateException("Seats Per Row must be greater than zero.");
        }

        if (seatPitchInch != null && seatPitchInch <= 0) {
            throw new IllegalStateException("Seat Pitch must be greater than zero.");
        }

        if (seatWidthInch != null && seatWidthInch <= 0) {
            throw new IllegalStateException("Seat Width must be greater than zero.");
        }

        if (crewSeats != null && crewSeats < 0) {
            throw new IllegalStateException("Crew Seats cannot be negative.");
        }
    }
}