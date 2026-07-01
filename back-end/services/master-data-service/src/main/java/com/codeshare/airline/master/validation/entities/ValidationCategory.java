package com.codeshare.airline.master.validation.entities;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity(name = "Validation_Category")
@Table(
        name = "VALIDATION_CATEGORY",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_VALIDATION_CATEGORY", columnNames = "CATEGORY_CODE")
        },
        indexes = {
                @Index(name = "IDX_VALIDATION_CATEGORY_STATUS", columnList = "STATUS")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ValidationCategory extends CSMDataAbstractEntity {
    @Column(name = "CATEGORY_CODE", nullable = false, length = 30)
    private String categoryCode;

    @Column(name = "CATEGORY_NAME", nullable = false, length = 150)
    private String categoryName;


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
        if (categoryCode != null) {
            categoryCode = categoryCode.trim().toUpperCase();
        }

        if (categoryName != null) {
            categoryName = categoryName.trim();
        }

        if (effectiveFrom != null && effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {
            throw new IllegalStateException("Invalid effective period.");
        }
    }
}