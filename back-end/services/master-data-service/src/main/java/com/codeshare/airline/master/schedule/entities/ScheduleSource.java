package com.codeshare.airline.master.schedule.entities;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity(name = "Schedule_Source")
@Table(
        name = "SCHEDULE_SOURCE",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_SCHEDULE_SOURCE", columnNames = "SOURCE_CODE")
        },
        indexes = {
                @Index(name = "IDX_SCHEDULE_SOURCE_STATUS", columnList = "STATUS")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ScheduleSource extends CSMDataAbstractEntity {
    @Column(name = "SOURCE_CODE", nullable = false, length = 20)
    private String sourceCode;

    @Column(name = "SOURCE_NAME", nullable = false, length = 100)
    private String sourceName;


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
        if (sourceCode != null) {
            sourceCode = sourceCode.trim().toUpperCase();
        }

        if (sourceName != null) {
            sourceName = sourceName.trim();
        }

        if (effectiveFrom != null && effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {
            throw new IllegalStateException("Invalid effective period.");
        }
    }
}