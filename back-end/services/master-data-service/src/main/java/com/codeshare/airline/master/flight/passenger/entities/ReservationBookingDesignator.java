package com.codeshare.airline.master.flight.passenger.entities;

import com.codeshare.airline.core.enums.common.CabinClass;
import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "RESERVATION_BOOKING_DESIGNATOR",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_RESERVATION_BOOKING_DESIGNATOR",
                        columnNames = "BOOKING_DESIGNATOR"
                )
        },
        indexes = {

                @Index(
                        name = "IDX_RESERVATION_BOOKING_DESIGNATOR_STATUS",
                        columnList = "STATUS"
                ),

                @Index(
                        name = "IDX_RESERVATION_BOOKING_DESIGNATOR_ACTIVE",
                        columnList = "ACTIVE"
                ),

                @Index(
                        name = "IDX_RESERVATION_BOOKING_DESIGNATOR_CABIN",
                        columnList = "CABIN_CLASS"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ReservationBookingDesignator extends CSMDataAbstractEntity {

    /**
     * Reservation Booking Designator (RBD)
     * Examples:
     * F, A, J, C, D, Z, W, Y, B, M, K, L, Q
     */
    @Column(name = "BOOKING_DESIGNATOR", nullable = false, length = 1)
    private String bookingDesignator;

    /**
     * Display name.
     */
    @Column(name = "BOOKING_NAME", nullable = false, length = 100)
    private String bookingName;

    /**
     * Cabin associated with the RBD.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "CABIN_CLASS", nullable = false, length = 30)
    private CabinClass cabinClass;

    /**
     * Optional business grouping.
     * Examples:
     * FULL_FARE
     * DISCOUNT
     * AWARD
     * GROUP
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

        if (bookingDesignator == null || bookingDesignator.isBlank()) {
            throw new IllegalStateException("Booking Designator is mandatory.");
        }

        if (bookingName == null || bookingName.isBlank()) {
            throw new IllegalStateException("Booking Name is mandatory.");
        }

        if (cabinClass == null) {
            throw new IllegalStateException("Cabin Class is mandatory.");
        }

        bookingDesignator = bookingDesignator.trim().toUpperCase();

        if (!bookingDesignator.matches("[A-Z]")) {
            throw new IllegalStateException(
                    "Booking Designator must be a single alphabetic character (A-Z)."
            );
        }

        bookingName = bookingName.trim();

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