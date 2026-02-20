package com.codeshare.airline.data.codeshare.eitities;

import com.codeshare.airline.core.enums.common.FlightNumberPattern;
import com.codeshare.airline.core.enums.common.Status;
import com.codeshare.airline.persistence.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "MASTER_CODESHARE_DAY_RULE",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_CS_DAY_RULE",
                        columnNames = {
                                "FLIGHT_MAPPING_ID",
                                "EFFECTIVE_FROM"
                        }
                )
        },
        indexes = {
                @Index(name = "IDX_CS_DAY_MAPPING", columnList = "FLIGHT_MAPPING_ID"),
                @Index(name = "IDX_CS_DAY_STATUS", columnList = "STATUS_CODE")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class CodeshareDayRule extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "FLIGHT_MAPPING_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_CS_DAY_RULE_MAPPING")
    )
    private CodeshareFlightMapping flightMapping;

    @Enumerated(EnumType.STRING)
    @Column(name = "FLIGHT_NUMBER_PATTERN", nullable = false, length = 10)
    private FlightNumberPattern flightNumberPattern;
    // EVEN / ODD / BOTH

    // Example: "1234567" (Mon–Sun)
    @Column(name = "OPERATING_DAYS", length = 7)
    private String operatingDays;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS_CODE", nullable = false, length = 20)
    private Status statusCode;

    @Column(name = "EFFECTIVE_FROM", nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "EFFECTIVE_TO")
    private LocalDate effectiveTo;


    @PrePersist
    @PreUpdate
    private void validate() {

        if (operatingDays != null) {

            if (!operatingDays.matches("[01]{7}")) {
                throw new IllegalStateException(
                        "Operating days must be 7 characters of 0 or 1 (Mon–Sun)."
                );
            }
        }

        if (effectiveFrom != null && effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {
            throw new IllegalStateException("Invalid effective period.");
        }
    }
}
