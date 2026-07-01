package com.codeshare.airline.master.reference.entities;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity(name = "Calendar")
@Table(
        name = "CALENDAR",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_CALENDAR", columnNames = "CALENDAR_CODE")
        },
        indexes = {
                @Index(name = "IDX_CALENDAR_STATUS", columnList = "STATUS")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Calendar extends CSMDataAbstractEntity {
    @Column(name = "CALENDAR_CODE", nullable = false, length = 30)
    private String calendarCode;

    @Column(name = "CALENDAR_NAME", nullable = false, length = 150)
    private String calendarName;

    @Column(name = "CALENDAR_TYPE", length = 30)
    private String calendarType;


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
        if (calendarCode != null) {
            calendarCode = calendarCode.trim().toUpperCase();
        }

        if (calendarName != null) {
            calendarName = calendarName.trim();
        }

        if (effectiveFrom != null && effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {
            throw new IllegalStateException("Invalid effective period.");
        }
    }
}