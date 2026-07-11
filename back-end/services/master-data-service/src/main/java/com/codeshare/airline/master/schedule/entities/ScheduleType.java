package com.codeshare.airline.master.schedule.entities;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity(name = "Schedule_Type")
@Table(
        name = "SCHEDULE_TYPE",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_SCHEDULE_TYPE", columnNames = "SCHEDULE_TYPE_CODE")
        },
        indexes = {
                @Index(name = "IDX_SCHEDULE_TYPE_STATUS", columnList = "STATUS")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ScheduleType extends CSMDataAbstractEntity {
    @Column(name = "SCHEDULE_TYPE_CODE", nullable = false, length = 10)
    private String scheduleTypeCode;

    @Column(name = "SCHEDULE_TYPE_NAME", nullable = false, length = 100)
    private String scheduleTypeName;


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
        if (scheduleTypeCode != null) {
            scheduleTypeCode = scheduleTypeCode.trim().toUpperCase();
        }

        if (scheduleTypeName != null) {
            scheduleTypeName = scheduleTypeName.trim();
        }

        if (effectiveFrom != null && effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {
            throw new IllegalStateException("Invalid effective period.");
        }
    }
}