package com.codeshare.airline.master.aircraft.entities;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.master.aircraft.entities.enums.AircraftOwnerType;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import com.codeshare.airline.master.georegion.eitities.Country;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "AIRCRAFT_OWNER",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_AIRCRAFT_OWNER_CODE",
                        columnNames = "OWNER_CODE"
                )
        },
        indexes = {
                @Index(
                        name = "IDX_AIRCRAFT_OWNER_STATUS",
                        columnList = "STATUS"
                ),
                @Index(
                        name = "IDX_AIRCRAFT_OWNER_TYPE",
                        columnList = "OWNER_TYPE"
                ),
                @Index(
                        name = "IDX_AIRCRAFT_OWNER_ACTIVE",
                        columnList = "ACTIVE"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class AircraftOwner extends CSMDataAbstractEntity {

    @Column(name = "OWNER_CODE", nullable = false, length = 20)
    private String ownerCode;

    @Column(name = "OWNER_NAME", nullable = false, length = 150)
    private String ownerName;

    @Enumerated(EnumType.STRING)
    @Column(name = "OWNER_TYPE", nullable = false, length = 30)
    private AircraftOwnerType ownerType;

    @Column(name = "IATA_CODE", length = 3)
    private String iataCode;

    @Column(name = "ICAO_CODE", length = 3)
    private String icaoCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "COUNTRY_ID",
            foreignKey = @ForeignKey(name = "FK_AIRCRAFT_OWNER_COUNTRY")
    )
    private Country country;

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

        if (ownerCode == null || ownerCode.isBlank()) {
            throw new IllegalStateException("Owner Code is mandatory.");
        }

        if (ownerName == null || ownerName.isBlank()) {
            throw new IllegalStateException("Owner Name is mandatory.");
        }

        if (ownerType == null) {
            throw new IllegalStateException("Owner Type is mandatory.");
        }

        ownerCode = ownerCode.trim().toUpperCase();
        ownerName = ownerName.trim();

        if (iataCode != null) {
            iataCode = iataCode.trim().toUpperCase();
        }

        if (icaoCode != null) {
            icaoCode = icaoCode.trim().toUpperCase();
        }

        if (effectiveFrom != null &&
                effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {
            throw new IllegalStateException(
                    "Effective From cannot be after Effective To."
            );
        }
    }
}
