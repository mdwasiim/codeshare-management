package com.codeshare.airline.master.aircraft.entities;

import com.codeshare.airline.platform.core.enums.master.aircraft.ConfigurationSource;
import com.codeshare.airline.platform.data.jpa.entity.CSMMasterDataEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "AIRCRAFT_CONFIGURATION",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_AIRCRAFT_CONFIGURATION_CODE",
                        columnNames = "CONFIGURATION_CODE"
                )
        },
        indexes = {
                @Index(
                        name = "IDX_AIRCRAFT_CONFIGURATION_TYPE",
                        columnList = "AIRCRAFT_TYPE_ID"
                ),
                @Index(
                        name = "IDX_AIRCRAFT_CONFIGURATION_CODE",
                        columnList = "CONFIGURATION_CODE"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class AircraftConfiguration extends CSMMasterDataEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "AIRCRAFT_TYPE_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_CONFIG_AIRCRAFT_TYPE")
    )
    private AircraftType aircraftType;

    /**
     * Aircraft Configuration Version (ACV)
     * Example:
     * QR359A
     * QR359B
     */
    @NotBlank
    @Column(name = "CONFIGURATION_CODE", nullable = false, length = 30)
    private String configurationCode;

    @NotBlank
    @Column(name = "CONFIGURATION_NAME", nullable = false, length = 150)
    private String configurationName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "CONFIGURATION_SOURCE", nullable = false, length = 20)
    private ConfigurationSource configurationSource = ConfigurationSource.MANUAL;

    @PositiveOrZero
    @Column(name = "FIRST_CLASS_SEATS", nullable = false)
    private Integer firstClassSeats = 0;

    @PositiveOrZero
    @Column(name = "BUSINESS_CLASS_SEATS", nullable = false)
    private Integer businessClassSeats = 0;

    @PositiveOrZero
    @Column(name = "PREMIUM_ECONOMY_SEATS", nullable = false)
    private Integer premiumEconomySeats = 0;

    @PositiveOrZero
    @Column(name = "ECONOMY_CLASS_SEATS", nullable = false)
    private Integer economyClassSeats = 0;

    @PositiveOrZero
    @Column(name = "TOTAL_SEATS", nullable = false)
    private Integer totalSeats = 0;

    @PositiveOrZero
    @Column(name = "CARGO_CAPACITY_KG")
    private Integer cargoCapacityKg;

    @Column(name = "DISPLAY_ORDER")
    private Integer displayOrder = 1;

    @PrePersist
    @PreUpdate
    private void normalize() {

        configurationCode = configurationCode.trim().toUpperCase();
        configurationName = configurationName.trim();

        totalSeats =
                (firstClassSeats == null ? 0 : firstClassSeats)
                        + (businessClassSeats == null ? 0 : businessClassSeats)
                        + (premiumEconomySeats == null ? 0 : premiumEconomySeats)
                        + (economyClassSeats == null ? 0 : economyClassSeats);
    }
}