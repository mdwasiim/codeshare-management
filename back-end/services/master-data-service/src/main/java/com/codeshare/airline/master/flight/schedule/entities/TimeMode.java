package com.codeshare.airline.master.flight.schedule.entities;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(
        name = "TIME_MODE",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_TIME_MODE",
                        columnNames = "TIME_MODE_CODE"
                )
        },
        indexes = {

                @Index(
                        name = "IDX_TIME_MODE_STATUS",
                        columnList = "STATUS"
                ),

                @Index(
                        name = "IDX_TIME_MODE_ACTIVE",
                        columnList = "ACTIVE"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class TimeMode extends CSMDataAbstractEntity {

    /**
     * IATA Time Mode
     *
     * L = Local Time
     * U = UTC
     */
    @Column(name = "TIME_MODE_CODE", nullable = false, length = 1)
    private String timeModeCode;

    @Column(name = "TIME_MODE_NAME", nullable = false, length = 50)
    private String timeModeName;

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

        if (timeModeCode == null || timeModeCode.isBlank()) {
            throw new IllegalStateException("Time Mode Code is mandatory.");
        }

        if (timeModeName == null || timeModeName.isBlank()) {
            throw new IllegalStateException("Time Mode Name is mandatory.");
        }

        timeModeCode = timeModeCode.trim().toUpperCase();
        timeModeName = timeModeName.trim();

        if (!Set.of("L", "U").contains(timeModeCode)) {
            throw new IllegalStateException(
                    "Time Mode must be 'L' (Local) or 'U' (UTC)."
            );
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