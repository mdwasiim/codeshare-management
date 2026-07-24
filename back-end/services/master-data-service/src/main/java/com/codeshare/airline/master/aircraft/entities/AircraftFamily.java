package com.codeshare.airline.master.aircraft.entities;

import com.codeshare.airline.platform.data.jpa.entity.CSMMasterDataEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "AIRCRAFT_FAMILY",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_AIRCRAFT_FAMILY_CODE",
                        columnNames = "FAMILY_CODE"
                ),
                @UniqueConstraint(
                        name = "UK_AIRCRAFT_FAMILY_NAME",
                        columnNames = "FAMILY_NAME"
                )
        },
        indexes = {
                @Index(
                        name = "IDX_AIRCRAFT_FAMILY_CODE",
                        columnList = "FAMILY_CODE"
                ),
                @Index(
                        name = "IDX_AIRCRAFT_FAMILY_NAME",
                        columnList = "FAMILY_NAME"
                ),
                @Index(
                        name = "IDX_AIRCRAFT_FAMILY_MANUFACTURER",
                        columnList = "MANUFACTURER_ID"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class AircraftFamily extends CSMMasterDataEntity {

    /**
     * Examples:
     * A320
     * A330
     * A350
     * B737
     * B777
     * B787
     */
    @NotBlank
    @Column(name = "FAMILY_CODE", nullable = false, length = 20)
    private String familyCode;

    /**
     * Display Name.
     */
    @NotBlank
    @Column(name = "FAMILY_NAME", nullable = false, length = 150)
    private String familyName;

    /**
     * Manufacturer.
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "MANUFACTURER_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_AIRCRAFT_FAMILY_MANUFACTURER")
    )
    private AircraftManufacturer manufacturer;

    /**
     * Display order for UI.
     */
    @Column(name = "DISPLAY_ORDER")
    private Integer displayOrder = 1;

    @PrePersist
    @PreUpdate
    private void normalize() {

        familyCode = familyCode.trim().toUpperCase();
        familyName = familyName.trim();
    }
}