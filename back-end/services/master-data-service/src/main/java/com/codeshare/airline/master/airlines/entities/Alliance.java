package com.codeshare.airline.master.airlines.entities;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import com.codeshare.airline.master.geography.entities.City;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "ALLIANCE",
        uniqueConstraints = {

                @UniqueConstraint(
                        name = "UK_ALLIANCE_CODE",
                        columnNames = "ALLIANCE_CODE"
                ),

                @UniqueConstraint(
                        name = "UK_ALLIANCE_NAME",
                        columnNames = "ALLIANCE_NAME"
                )
        },
        indexes = {

                @Index(
                        name = "IDX_ALLIANCE_STATUS",
                        columnList = "STATUS"
                ),

                @Index(
                        name = "IDX_ALLIANCE_ACTIVE",
                        columnList = "ACTIVE"
                ),

                @Index(
                        name = "IDX_ALLIANCE_IATA",
                        columnList = "IATA_CODE"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Alliance extends CSMDataAbstractEntity {

    /**
     * STAR
     * ONEWORLD
     * SKYTEAM
     */
    @Column(name = "ALLIANCE_CODE", nullable = false, length = 20)
    private String allianceCode;

    /**
     * Star Alliance
     * oneworld
     * SkyTeam
     */
    @Column(name = "ALLIANCE_NAME", nullable = false, length = 150)
    private String allianceName;

    /**
     * Optional business code
     */
    @Column(name = "IATA_CODE", length = 10)
    private String iataCode;

    @Column(name = "WEBSITE", length = 250)
    private String website;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "HEADQUARTERS_CITY_ID",
            foreignKey = @ForeignKey(name = "FK_ALLIANCE_HEADQUARTERS_CITY")
    )
    private City headquartersCity;

    @Column(name = "FOUNDED_DATE")
    private LocalDate foundedDate;

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
    private void validateAndNormalize() {

        if (allianceCode == null || allianceCode.isBlank()) {
            throw new IllegalStateException("Alliance Code is mandatory.");
        }

        if (allianceName == null || allianceName.isBlank()) {
            throw new IllegalStateException("Alliance Name is mandatory.");
        }

        allianceCode = allianceCode.trim().toUpperCase();
        allianceName = allianceName.trim();

        if (iataCode != null) {
            iataCode = iataCode.trim().toUpperCase();
        }

        if (website != null) {
            website = website.trim();
        }

        if (description != null) {
            description = description.trim();
        }

        if (remarks != null) {
            remarks = remarks.trim();
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