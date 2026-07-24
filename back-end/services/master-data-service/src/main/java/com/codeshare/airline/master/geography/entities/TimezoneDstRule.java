package com.codeshare.airline.master.geography.entities;

import com.codeshare.airline.platform.data.jpa.entity.CSMMasterDataEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.Month;

@Entity
@Table(
        name = "TIMEZONE_DST_RULE",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_DST_RULE_TIMEZONE_NAME",
                        columnNames = {"TIMEZONE_ID", "RULE_NAME"}
                )
        },
        indexes = {
                @Index(name = "IDX_DST_RULE_TIMEZONE", columnList = "TIMEZONE_ID"),
                @Index(name = "IDX_DST_RULE_STATUS", columnList = "STATUS")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class TimezoneDstRule extends CSMMasterDataEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "TIMEZONE_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_DST_RULE_TIMEZONE")
    )
    private Timezone timezone;

    @Column(name = "RULE_NAME", nullable = false, length = 100)
    private String ruleName;

    @Enumerated(EnumType.STRING)
    @Column(name = "START_MONTH", nullable = false, length = 20)
    private Month startMonth;

    @Column(name = "START_WEEK_OF_MONTH", nullable = false)
    private Integer startWeekOfMonth;

    @Enumerated(EnumType.STRING)
    @Column(name = "START_DAY_OF_WEEK", nullable = false, length = 20)
    private DayOfWeek startDayOfWeek;

    @Column(name = "START_TIME", nullable = false)
    private LocalTime startTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "END_MONTH", nullable = false, length = 20)
    private Month endMonth;

    @Column(name = "END_WEEK_OF_MONTH", nullable = false)
    private Integer endWeekOfMonth;

    @Enumerated(EnumType.STRING)
    @Column(name = "END_DAY_OF_WEEK", nullable = false, length = 20)
    private DayOfWeek endDayOfWeek;

    @Column(name = "END_TIME", nullable = false)
    private LocalTime endTime;

    @Column(name = "DST_OFFSET_MINUTES", nullable = false)
    private Integer dstOffsetMinutes;

    @PrePersist
    @PreUpdate
    private void normalize() {

        if (ruleName != null) {
            ruleName = ruleName.trim();
        }

        if (startWeekOfMonth == null || startWeekOfMonth < -1 || startWeekOfMonth > 5) {
            throw new IllegalStateException("Start week of month must be between 1-5 or -1 for last week.");
        }

        if (endWeekOfMonth == null || endWeekOfMonth < -1 || endWeekOfMonth > 5) {
            throw new IllegalStateException("End week of month must be between 1-5 or -1 for last week.");
        }

        if (dstOffsetMinutes == null || dstOffsetMinutes <= 0) {
            throw new IllegalStateException("DST offset must be greater than zero.");
        }
    }
}