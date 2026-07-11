package com.codeshare.airline.master.schedule.entities;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity(name = "Schedule_Status")
@Table(
        name = "SCHEDULE_STATUS",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_SCHEDULE_STATUS", columnNames = "SCHEDULE_STATUS_CODE")
        },
        indexes = {
                @Index(name = "IDX_SCHEDULE_STATUS_STATUS", columnList = "STATUS")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ScheduleStatus extends CSMDataAbstractEntity {
    @Column(name = "SCHEDULE_STATUS_CODE", nullable = false, length = 20)
    private String scheduleStatusCode;

    @Column(name = "SCHEDULE_STATUS_NAME", nullable = false, length = 100)
    private String scheduleStatusName;


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
        if (scheduleStatusCode != null) {
            scheduleStatusCode = scheduleStatusCode.trim().toUpperCase();
        }

        if (scheduleStatusName != null) {
            scheduleStatusName = scheduleStatusName.trim();
        }

        if (effectiveFrom != null && effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {
            throw new IllegalStateException("Invalid effective period.");
        }
    }
}