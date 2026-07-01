package com.codeshare.airline.master.flightcommercial.schedule.entities;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(
        name = "FLIGHT_FREQUENCY",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_FLIGHT_FREQUENCY",
                        columnNames = "FREQUENCY_CODE"
                )
        },
        indexes = {

                @Index(
                        name = "IDX_FLIGHT_FREQUENCY_STATUS",
                        columnList = "STATUS"
                ),

                @Index(
                        name = "IDX_FLIGHT_FREQUENCY_ACTIVE",
                        columnList = "ACTIVE"
                ),

                @Index(
                        name = "IDX_FLIGHT_FREQUENCY_OPERATING_DAYS",
                        columnList = "OPERATING_DAYS"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class FlightFrequency extends CSMDataAbstractEntity {

    /**
     * IATA Frequency Pattern
     *
     * Examples:
     * 1234567
     * 12345
     * 67
     * 135
     * 246
     */
    @Column(name = "FREQUENCY_CODE", nullable = false, length = 7)
    private String frequencyCode;

    /**
     * Display Name
     *
     * Examples:
     * Daily
     * Weekdays
     * Weekends
     * Mon Wed Fri
     */
    @Column(name = "FREQUENCY_NAME", nullable = false, length = 100)
    private String frequencyName;

    /**
     * Number of operating days.
     * Automatically derived from Frequency Code.
     */
    @Column(name = "OPERATING_DAYS", nullable = false)
    private Integer operatingDays;

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

        if (frequencyCode == null || frequencyCode.isBlank()) {
            throw new IllegalStateException("Frequency Code is mandatory.");
        }

        if (frequencyName == null || frequencyName.isBlank()) {
            throw new IllegalStateException("Frequency Name is mandatory.");
        }

        frequencyCode = frequencyCode.trim();
        frequencyName = frequencyName.trim();

        if (!frequencyCode.matches("[1-7]{1,7}")) {
            throw new IllegalStateException(
                    "Frequency Code must contain only digits 1-7."
            );
        }

        Set<Character> uniqueDays = new java.util.LinkedHashSet<>();

        for (char day : frequencyCode.toCharArray()) {
            if (!uniqueDays.add(day)) {
                throw new IllegalStateException(
                        "Duplicate operating day found in Frequency Code."
                );
            }
        }

        operatingDays = uniqueDays.size();

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

    /*
     * Convenience methods.
     * Not persisted.
     */

    @Transient
    public boolean operatesOnMonday() {
        return frequencyCode != null && frequencyCode.contains("1");
    }

    @Transient
    public boolean operatesOnTuesday() {
        return frequencyCode != null && frequencyCode.contains("2");
    }

    @Transient
    public boolean operatesOnWednesday() {
        return frequencyCode != null && frequencyCode.contains("3");
    }

    @Transient
    public boolean operatesOnThursday() {
        return frequencyCode != null && frequencyCode.contains("4");
    }

    @Transient
    public boolean operatesOnFriday() {
        return frequencyCode != null && frequencyCode.contains("5");
    }

    @Transient
    public boolean operatesOnSaturday() {
        return frequencyCode != null && frequencyCode.contains("6");
    }

    @Transient
    public boolean operatesOnSunday() {
        return frequencyCode != null && frequencyCode.contains("7");
    }
}