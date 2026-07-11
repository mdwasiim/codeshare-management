package com.codeshare.airline.master.schedule.entities;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.core.enums.schedule.SeasonType;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "SEASON",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_SEASON_CODE", columnNames = "SEASON_CODE")
        },
        indexes = {
                @Index(name = "IDX_SEASON_CODE", columnList = "SEASON_CODE"),
                @Index(name = "IDX_SEASON_TYPE", columnList = "SEASON_TYPE"),
                @Index(name = "IDX_SEASON_YEAR", columnList = "SCHEDULE_YEAR"),
                @Index(name = "IDX_SEASON_STATUS", columnList = "STATUS")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Season extends CSMDataAbstractEntity {

    @Column(name = "SEASON_CODE", nullable = false, length = 10)
    private String seasonCode;   // S25, W25

    @Column(name = "SEASON_NAME", nullable = false, length = 100)
    private String seasonName;   // Summer 2025

    @Enumerated(EnumType.STRING)
    @Column(name = "SEASON_TYPE", nullable = false, length = 10)
    private SeasonType seasonType;

    @Column(name = "SCHEDULE_YEAR", nullable = false)
    private Integer scheduleYear;

    @Column(name = "SEASON_START_DATE", nullable = false)
    private LocalDate seasonStartDate;

    @Column(name = "SEASON_END_DATE", nullable = false)
    private LocalDate seasonEndDate;

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
        if (seasonCode == null || seasonCode.isBlank()) {
            throw new IllegalStateException("Season Code is mandatory.");
        }

        if (seasonName == null || seasonName.isBlank()) {
            throw new IllegalStateException("Season Name is mandatory.");
        }

        if (seasonType == null) {
            throw new IllegalStateException("Season Type is mandatory.");
        }

        if (scheduleYear == null) {
            throw new IllegalStateException("Schedule Year is mandatory.");
        }

        if (seasonStartDate == null || seasonEndDate == null) {
            throw new IllegalStateException("Season start and end dates are mandatory.");
        }

        seasonCode = seasonCode.trim().toUpperCase();
        seasonName = seasonName.trim();

        if (description != null) {
            description = description.trim();
        }

        if (remarks != null) {
            remarks = remarks.trim();
        }

        if (seasonStartDate.isAfter(seasonEndDate)) {
            throw new IllegalStateException("Season Start Date cannot be after Season End Date.");
        }

        if (effectiveFrom != null &&
                effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {
            throw new IllegalStateException("Effective From cannot be after Effective To.");
        }
    }
}
