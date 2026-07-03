package com.codeshare.airline.master.aircraft.entities;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import com.codeshare.airline.master.geography.entities.Country;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "AIRCRAFT_MANUFACTURER",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_AIRCRAFT_MANUFACTURER_CODE",
                        columnNames = "MANUFACTURER_CODE"
                ),
                @UniqueConstraint(
                        name = "UK_AIRCRAFT_MANUFACTURER_NAME",
                        columnNames = "MANUFACTURER_NAME"
                )
        },
        indexes = {
                @Index(
                        name = "IDX_AIRCRAFT_MANUFACTURER_STATUS",
                        columnList = "STATUS"
                ),
                @Index(
                        name = "IDX_AIRCRAFT_MANUFACTURER_ACTIVE",
                        columnList = "ACTIVE"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class AircraftManufacturer extends CSMDataAbstractEntity {

    @Column(name = "MANUFACTURER_CODE", nullable = false, length = 20)
    private String manufacturerCode;

    @Column(name = "MANUFACTURER_NAME", nullable = false, length = 150)
    private String manufacturerName;

    @Column(name = "SHORT_NAME", length = 50)
    private String shortName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "COUNTRY_ID",
            foreignKey = @ForeignKey(name = "FK_MANUFACTURER_COUNTRY")
    )
    private Country country;

    @Column(name = "WEBSITE", length = 250)
    private String website;

    @Column(name = "ACTIVE", nullable = false)
    private Boolean active = Boolean.TRUE;

    @Column(name = "DISPLAY_ORDER")
    private Integer displayOrder = 1;

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

        if (manufacturerCode == null || manufacturerCode.isBlank()) {
            throw new IllegalStateException("Manufacturer Code is mandatory.");
        }

        if (manufacturerName == null || manufacturerName.isBlank()) {
            throw new IllegalStateException("Manufacturer Name is mandatory.");
        }

        manufacturerCode = manufacturerCode.trim().toUpperCase();
        manufacturerName = manufacturerName.trim();

        if (shortName != null) {
            shortName = shortName.trim();
        }

        if (website != null) {
            website = website.trim();
        }

        if (effectiveFrom != null &&
                effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {
            throw new IllegalStateException("Effective From cannot be after Effective To.");
        }
    }
}