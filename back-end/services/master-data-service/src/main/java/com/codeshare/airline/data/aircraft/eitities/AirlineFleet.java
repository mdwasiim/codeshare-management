package com.codeshare.airline.data.aircraft.eitities;

import com.codeshare.airline.core.enums.common.Status;
import com.codeshare.airline.data.core.eitities.AirlineCarrier;
import com.codeshare.airline.persistence.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "MASTER_AIRLINE_FLEET",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_AIRLINE_CONFIG",
                        columnNames = {"AIRLINE_ID", "AIRCRAFT_CONFIG_ID"}
                )
        },
        indexes = {
                @Index(name = "IDX_FLEET_AIRLINE", columnList = "AIRLINE_ID"),
                @Index(name = "IDX_FLEET_CONFIG", columnList = "AIRCRAFT_CONFIG_ID"),
                @Index(name = "IDX_FLEET_STATUS", columnList = "STATUS_CODE")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class AirlineFleet extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "AIRLINE_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_FLEET_AIRLINE")
    )
    private AirlineCarrier airline;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "AIRCRAFT_CONFIG_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_FLEET_CONFIG")
    )
    private AircraftConfiguration aircraftConfiguration;

    // Number of aircraft of this configuration
    @Column(name = "AIRCRAFT_COUNT", nullable = false)
    private Integer aircraftCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS_CODE", nullable = false, length = 20)
    private Status statusCode;

    @Column(name = "EFFECTIVE_FROM")
    private LocalDate effectiveFrom;

    @Column(name = "EFFECTIVE_TO")
    private LocalDate effectiveTo;

    @PrePersist
    @PreUpdate
    private void validate() {

        if (aircraftCount == null || aircraftCount <= 0) {
            throw new IllegalStateException("Aircraft count must be greater than zero.");
        }

        if (effectiveFrom != null && effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {
            throw new IllegalStateException("Invalid effective period.");
        }
    }
}
