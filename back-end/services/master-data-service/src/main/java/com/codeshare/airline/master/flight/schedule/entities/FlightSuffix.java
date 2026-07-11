package com.codeshare.airline.master.flight.schedule.entities;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "FLIGHT_SUFFIX",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_FLIGHT_SUFFIX",
                        columnNames = "SUFFIX_CODE"
                )
        },
        indexes = {

                @Index(
                        name = "IDX_FLIGHT_SUFFIX_STATUS",
                        columnList = "STATUS"
                ),

                @Index(
                        name = "IDX_FLIGHT_SUFFIX_ACTIVE",
                        columnList = "ACTIVE"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class FlightSuffix extends CSMDataAbstractEntity {

    /**
     * IATA Flight Suffix
     *
     * Examples:
     * A
     * B
     * C
     */
    @Column(name = "SUFFIX_CODE", nullable = false, length = 1)
    private String suffixCode;

    /**
     * Display Name
     */
    @Column(name = "SUFFIX_NAME", nullable = false, length = 100)
    private String suffixName;

    /**
     * Business meaning of the suffix.
     */
    @Column(name = "SUFFIX_MEANING", length = 250)
    private String suffixMeaning;

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

        if (suffixCode == null || suffixCode.isBlank()) {
            throw new IllegalStateException("Suffix Code is mandatory.");
        }

        if (suffixName == null || suffixName.isBlank()) {
            throw new IllegalStateException("Suffix Name is mandatory.");
        }

        suffixCode = suffixCode.trim().toUpperCase();
        suffixName = suffixName.trim();

        if (!suffixCode.matches("[A-Z]")) {
            throw new IllegalStateException(
                    "Suffix Code must be a single alphabetic character (A-Z)."
            );
        }

        if (suffixMeaning != null) {
            suffixMeaning = suffixMeaning.trim();
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