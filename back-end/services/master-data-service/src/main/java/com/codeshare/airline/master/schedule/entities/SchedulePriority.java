package com.codeshare.airline.master.schedule.entities;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity(name = "Schedule_Priority")
@Table(
        name = "SCHEDULE_PRIORITY",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_SCHEDULE_PRIORITY", columnNames = "PRIORITY_CODE")
        },
        indexes = {
                @Index(name = "IDX_SCHEDULE_PRIORITY_STATUS", columnList = "STATUS")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class SchedulePriority extends CSMDataAbstractEntity {
    @Column(name = "PRIORITY_CODE", nullable = false, length = 10)
    private String priorityCode;

    @Column(name = "PRIORITY_NAME", nullable = false, length = 100)
    private String priorityName;

    @Column(name = "PRIORITY_LEVEL")
    private Integer priorityLevel;


    @Column(name = "DESCRIPTION", length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 20)
    private RecordStatus recordStatus;

    @Column(name = "EFFECTIVE_FROM")
    private LocalDate effectiveFrom;

    @Column(name = "EFFECTIVE_TO")
    private LocalDate effectiveTo;

    @PrePersist
    @PreUpdate
    private void normalizeAndValidate() {
        if (priorityCode != null) {
            priorityCode = priorityCode.trim().toUpperCase();
        }

        if (priorityName != null) {
            priorityName = priorityName.trim();
        }

        if (effectiveFrom != null && effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {
            throw new IllegalStateException("Invalid effective period.");
        }
    }
}