package com.codeshare.airline.master.flight.passenger.entities;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "RESERVATION_BOOKING_MODIFIER",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_RESERVATION_BOOKING_MODIFIER",
                        columnNames = "MODIFIER_CODE"
                )
        },
        indexes = {

                @Index(
                        name = "IDX_RESERVATION_BOOKING_MODIFIER_STATUS",
                        columnList = "STATUS"
                ),

                @Index(
                        name = "IDX_RESERVATION_BOOKING_MODIFIER_ACTIVE",
                        columnList = "ACTIVE"
                ),

                @Index(
                        name = "IDX_RESERVATION_BOOKING_MODIFIER_CATEGORY",
                        columnList = "CATEGORY"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ReservationBookingModifier extends CSMDataAbstractEntity {

    /**
     * Reservation Booking Modifier.
     *
     * Examples:
     * A
     * C
     * IN
     */
    @Column(name = "MODIFIER_CODE", nullable = false, length = 2)
    private String modifierCode;

    /**
     * Display name.
     */
    @Column(name = "MODIFIER_NAME", nullable = false, length = 100)
    private String modifierName;

    /**
     * Business grouping.
     * Examples:
     * PASSENGER_TYPE
     * DISCOUNT
     * CORPORATE
     * STAFF
     */
    @Column(name = "CATEGORY", length = 50)
    private String category;

    /**
     * Official IATA/business definition.
     */
    @Column(name = "IATA_DEFINITION", length = 1000)
    private String iataDefinition;

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

        if (modifierCode == null || modifierCode.isBlank()) {
            throw new IllegalStateException("Modifier Code is mandatory.");
        }

        if (modifierName == null || modifierName.isBlank()) {
            throw new IllegalStateException("Modifier Name is mandatory.");
        }

        modifierCode = modifierCode.trim().toUpperCase();

        if (modifierCode.length() > 2) {
            throw new IllegalStateException(
                    "Modifier Code cannot exceed 2 characters."
            );
        }

        modifierName = modifierName.trim();

        if (category != null) {
            category = category.trim().toUpperCase();
        }

        if (iataDefinition != null) {
            iataDefinition = iataDefinition.trim();
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