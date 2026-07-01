package com.codeshare.airline.master.reference.entities;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity(name = "Week_Day")
@Table(
        name = "WEEK_DAY",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_WEEK_DAY", columnNames = "WEEKDAY_CODE")
        },
        indexes = {
                @Index(name = "IDX_WEEK_DAY_STATUS", columnList = "STATUS")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class WeekDay extends CSMDataAbstractEntity {
    @Column(name = "WEEKDAY_CODE", nullable = false, length = 1)
    private String weekdayCode;

    @Column(name = "WEEKDAY_NAME", nullable = false, length = 20)
    private String weekdayName;

    @Column(name = "ISO_DAY_NUMBER")
    private Integer isoDayNumber;


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
        if (weekdayCode != null) {
            weekdayCode = weekdayCode.trim().toUpperCase();
        }

        if (weekdayName != null) {
            weekdayName = weekdayName.trim();
        }

        if (effectiveFrom != null && effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {
            throw new IllegalStateException("Invalid effective period.");
        }
    }
}