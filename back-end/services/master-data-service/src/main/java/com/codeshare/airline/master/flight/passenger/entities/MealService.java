package com.codeshare.airline.master.flight.passenger.entities;

import com.codeshare.airline.platform.core.enums.common.CabinClass;
import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "MEAL_SERVICE",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_MEAL_SERVICE",
                        columnNames = "MEAL_CODE"
                )
        },
        indexes = {

                @Index(
                        name = "IDX_MEAL_SERVICE_STATUS",
                        columnList = "STATUS"
                ),

                @Index(
                        name = "IDX_MEAL_SERVICE_ACTIVE",
                        columnList = "ACTIVE"
                ),

                @Index(
                        name = "IDX_MEAL_SERVICE_CATEGORY",
                        columnList = "MEAL_CATEGORY"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class MealService extends CSMDataAbstractEntity {

    /**
     * IATA Meal Code
     * Examples:
     * B, L, D, S, R
     */
    @Column(name = "MEAL_CODE", nullable = false, length = 2)
    private String mealCode;

    /**
     * Display Name
     */
    @Column(name = "MEAL_NAME", nullable = false, length = 100)
    private String mealName;

    /**
     * Business grouping.
     * BREAKFAST, LUNCH, DINNER, SNACK, REFRESHMENT
     */
    @Column(name = "MEAL_CATEGORY", length = 30)
    private String mealCategory;

    /**
     * Optional cabin applicability.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "CABIN_CLASS", length = 30)
    private CabinClass cabinClass;

    /**
     * Official IATA definition.
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

        if (mealCode == null || mealCode.isBlank()) {
            throw new IllegalStateException("Meal Code is mandatory.");
        }

        if (mealName == null || mealName.isBlank()) {
            throw new IllegalStateException("Meal Name is mandatory.");
        }

        mealCode = mealCode.trim().toUpperCase();
        mealName = mealName.trim();

        if (mealCategory != null) {
            mealCategory = mealCategory.trim().toUpperCase();
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