package com.codeshare.airline.data.codeshare.eitities;

import com.codeshare.airline.core.enums.codeshare.CodeshareDisclosureType;
import com.codeshare.airline.core.enums.codeshare.CodeshareMappingType;
import com.codeshare.airline.core.enums.common.Status;
import com.codeshare.airline.persistence.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(
        name = "MASTER_CODESHARE_FLIGHT_MAPPING",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_CS_MAPPING_RANGE",
                        columnNames = {
                                "ROUTE_ID",
                                "MARKETING_FLIGHT_START",
                                "OPERATING_FLIGHT_START",
                                "EFFECTIVE_FROM"
                        }
                )
        },
        indexes = {
                @Index(name = "IDX_CS_MAPPING_ROUTE", columnList = "ROUTE_ID"),
                @Index(name = "IDX_CS_MAPPING_STATUS", columnList = "STATUS_CODE")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class CodeshareFlightMapping extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "ROUTE_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_CS_MAPPING_ROUTE")
    )
    private CodeshareRoute route;

    @OneToMany(mappedBy = "flightMapping")
    private Set<CodeshareDayRule> dayRules;

    @OneToMany(mappedBy = "flightMapping")
    private Set<CodeshareEquipmentRule> equipmentRules;

    @OneToMany(mappedBy = "flightMapping")
    private Set<CodeshareDeiRule> deiRules;

    @Enumerated(EnumType.STRING)
    @Column(name = "MAPPING_TYPE", nullable = false, length = 20)
    private CodeshareMappingType mappingType;

    // Marketing flight number range
    @Column(name = "MARKETING_FLIGHT_START", nullable = false)
    private Integer marketingFlightStart;

    @Column(name = "MARKETING_FLIGHT_END", nullable = false)
    private Integer marketingFlightEnd;

    // Operating flight number range
    @Column(name = "OPERATING_FLIGHT_START", nullable = false)
    private Integer operatingFlightStart;

    @Column(name = "OPERATING_FLIGHT_END", nullable = false)
    private Integer operatingFlightEnd;

    // Optional flight suffix (e.g., A, B)
    @Column(name = "FLIGHT_SUFFIX", length = 1)
    private String flightSuffix;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS_CODE", nullable = false, length = 20)
    private Status statusCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "DISCLOSURE_TYPE", nullable = false, length = 30)
    private CodeshareDisclosureType disclosureType;

    @Column(name = "EFFECTIVE_FROM", nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "EFFECTIVE_TO")
    private LocalDate effectiveTo;

    @PrePersist
    @PreUpdate
    private void validate() {

        if (marketingFlightStart <= 0 || marketingFlightEnd <= 0 ||
                operatingFlightStart <= 0 || operatingFlightEnd <= 0) {
            throw new IllegalStateException("Flight numbers must be positive.");
        }

        if (marketingFlightStart > marketingFlightEnd) {
            throw new IllegalStateException("Invalid marketing flight range.");
        }

        if (operatingFlightStart > operatingFlightEnd) {
            throw new IllegalStateException("Invalid operating flight range.");
        }

        int marketingSize = marketingFlightEnd - marketingFlightStart;
        int operatingSize = operatingFlightEnd - operatingFlightStart;

        if (marketingSize != operatingSize) {
            throw new IllegalStateException(
                    "Marketing and operating ranges must match."
            );
        }

        if (flightSuffix != null && flightSuffix.length() > 1) {
            throw new IllegalStateException("Flight suffix must be one character.");
        }

        if (effectiveFrom != null && effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {
            throw new IllegalStateException("Invalid effective period.");
        }
    }
}
