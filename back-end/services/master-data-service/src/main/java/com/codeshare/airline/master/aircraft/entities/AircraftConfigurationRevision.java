package com.codeshare.airline.master.aircraft.entities;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import com.codeshare.airline.master.aircraft.entities.enums.ConfigurationRevisionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "AIRCRAFT_CONFIGURATION_REVISION",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_AIRCRAFT_CONFIGURATION_REVISION",
                        columnNames = {
                                "AIRCRAFT_CONFIGURATION_ID",
                                "REVISION_NUMBER"
                        }
                )
        },
        indexes = {
                @Index(
                        name = "IDX_AIRCRAFT_CONFIGURATION_REVISION_CONFIGURATION",
                        columnList = "AIRCRAFT_CONFIGURATION_ID"
                ),
                @Index(
                        name = "IDX_AIRCRAFT_CONFIGURATION_REVISION_STATUS",
                        columnList = "REVISION_STATUS"
                ),
                @Index(
                        name = "IDX_AIRCRAFT_CONFIGURATION_REVISION_ACTIVE",
                        columnList = "ACTIVE"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class AircraftConfigurationRevision extends CSMDataAbstractEntity {

    /**
     * Parent Aircraft Configuration (ACV)
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "AIRCRAFT_CONFIGURATION_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_CONFIGURATION_REVISION_CONFIGURATION")
    )
    private AircraftConfiguration aircraftConfiguration;

    /**
     * Revision Number
     * Example:
     * 1
     * 2
     * 3
     */
    @Column(name = "REVISION_NUMBER", nullable = false)
    private Integer revisionNumber;

    /**
     * Example:
     * REV-001
     * REV-002
     */
    @Column(name = "REVISION_CODE", nullable = false, length = 30)
    private String revisionCode;

    /**
     * Example:
     * Initial Configuration
     * Cabin Retrofit
     * Premium Cabin Upgrade
     */
    @Column(name = "REVISION_NAME", nullable = false, length = 150)
    private String revisionName;

    @Enumerated(EnumType.STRING)
    @Column(name = "REVISION_STATUS", nullable = false, length = 30)
    private ConfigurationRevisionStatus revisionStatus =
            ConfigurationRevisionStatus.DRAFT;

    /**
     * Why was this revision created?
     */
    @Column(name = "CHANGE_REASON", length = 500)
    private String changeReason;

    @Column(name = "PUBLISHED_DATE")
    private LocalDate publishedDate;

    @Column(name = "PUBLISHED_BY", length = 100)
    private String publishedBy;

    /**
     * Indicates the latest active revision.
     */
    @Column(name = "CURRENT_REVISION", nullable = false)
    private Boolean currentRevision = Boolean.TRUE;

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

        if (aircraftConfiguration == null) {
            throw new IllegalStateException("Aircraft Configuration is mandatory.");
        }

        if (revisionNumber == null || revisionNumber <= 0) {
            throw new IllegalStateException("Revision Number is mandatory.");
        }

        if (revisionCode == null || revisionCode.isBlank()) {
            throw new IllegalStateException("Revision Code is mandatory.");
        }

        if (revisionName == null || revisionName.isBlank()) {
            throw new IllegalStateException("Revision Name is mandatory.");
        }

        if (revisionStatus == null) {
            throw new IllegalStateException("Revision Status is mandatory.");
        }

        revisionCode = revisionCode.trim().toUpperCase();
        revisionName = revisionName.trim();

        if (publishedBy != null) {
            publishedBy = publishedBy.trim();
        }

        if (changeReason != null) {
            changeReason = changeReason.trim();
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