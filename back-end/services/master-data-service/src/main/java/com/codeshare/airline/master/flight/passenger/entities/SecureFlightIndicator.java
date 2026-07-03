package com.codeshare.airline.master.flight.passenger.entities;

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
        name = "SECURE_FLIGHT_INDICATOR",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_SECURE_FLIGHT_INDICATOR",
                        columnNames = "SECURE_FLIGHT_INDICATOR_CODE"
                )
        },
        indexes = {

                @Index(
                        name = "IDX_SECURE_FLIGHT_INDICATOR_STATUS",
                        columnList = "STATUS"
                ),

                @Index(
                        name = "IDX_SECURE_FLIGHT_INDICATOR_ACTIVE",
                        columnList = "ACTIVE"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class SecureFlightIndicator extends CSMDataAbstractEntity {

    /**
     * Examples:
     * Y
     * N
     */
    @Column(name = "SECURE_FLIGHT_INDICATOR_CODE", nullable = false, length = 1)
    private String secureFlightIndicatorCode;

    @Column(name = "SECURE_FLIGHT_INDICATOR_NAME", nullable = false, length = 100)
    private String secureFlightIndicatorName;

    /**
     * Regulatory authority defining this indicator.
     */
    @Column(name = "REGULATORY_AUTHORITY", length = 100)
    private String regulatoryAuthority;

    /**
     * Business meaning of the indicator.
     */
    @Column(name = "INDICATOR_MEANING", length = 500)
    private String indicatorMeaning;

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

        if (secureFlightIndicatorCode == null || secureFlightIndicatorCode.isBlank()) {
            throw new IllegalStateException("Secure Flight Indicator Code is mandatory.");
        }

        if (secureFlightIndicatorName == null || secureFlightIndicatorName.isBlank()) {
            throw new IllegalStateException("Secure Flight Indicator Name is mandatory.");
        }

        secureFlightIndicatorCode = secureFlightIndicatorCode.trim().toUpperCase();
        secureFlightIndicatorName = secureFlightIndicatorName.trim();

        // Restrict only if your business uses Y/N exclusively.
        if (!Set.of("Y", "N").contains(secureFlightIndicatorCode)) {
            throw new IllegalStateException(
                    "Secure Flight Indicator Code must be 'Y' or 'N'."
            );
        }

        if (regulatoryAuthority != null) {
            regulatoryAuthority = regulatoryAuthority.trim();
        }

        if (indicatorMeaning != null) {
            indicatorMeaning = indicatorMeaning.trim();
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