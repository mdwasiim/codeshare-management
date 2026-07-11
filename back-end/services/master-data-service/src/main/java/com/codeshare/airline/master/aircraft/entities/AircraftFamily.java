package com.codeshare.airline.master.aircraft.entities;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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
                        name = "IDX_AIRCRAFT_FAMILY_STATUS",
                        columnList = "STATUS"
                ),
                @Index(
                        name = "IDX_AIRCRAFT_FAMILY_MANUFACTURER_ID",
                        columnList = "MANUFACTURER_ID"
                ),
                @Index(
                        name = "IDX_AIRCRAFT_FAMILY_ACTIVE",
                        columnList = "ACTIVE"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class AircraftFamily extends CSMDataAbstractEntity {

    /**
     * Business key.
     * Examples:
     * A320
     * A330
     * A350
     * B737
     * B777
     * B787
     */
    @Column(name = "FAMILY_CODE", nullable = false, length = 20)
    private String familyCode;

    /**
     * Display Name
     */
    @Column(name = "FAMILY_NAME", nullable = false, length = 150)
    private String familyName;

    /**
     * Airbus
     * Boeing
     * Embraer
     * ATR
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "MANUFACTURER_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_AIRCRAFT_FAMILY_MANUFACTURER")
    )
    private AircraftManufacturer manufacturer;

    @Column(name = "DESCRIPTION", length = 500)
    private String description;

    @Column(name = "DISPLAY_ORDER")
    private Integer displayOrder = 1;

    @Column(name = "ACTIVE", nullable = false)
    private Boolean active = Boolean.TRUE;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    private RecordStatus recordStatus = RecordStatus.ACTIVE;

    @Column(name = "REMARKS", length = 1000)
    private String remarks;

    @Column(name = "EFFECTIVE_FROM")
    private LocalDate effectiveFrom;

    @Column(name = "EFFECTIVE_TO")
    private LocalDate effectiveTo;

    @PrePersist
    @PreUpdate
    private void validate() {

        if (familyCode == null || familyCode.isBlank()) {
            throw new IllegalStateException("Family Code is mandatory.");
        }

        if (familyName == null || familyName.isBlank()) {
            throw new IllegalStateException("Family Name is mandatory.");
        }

        if (manufacturer == null) {
            throw new IllegalStateException("Manufacturer is mandatory.");
        }

        familyCode = familyCode.trim().toUpperCase();
        familyName = familyName.trim();

        if (effectiveFrom != null &&
                effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {

            throw new IllegalStateException(
                    "Effective From cannot be after Effective To."
            );
        }
    }
}