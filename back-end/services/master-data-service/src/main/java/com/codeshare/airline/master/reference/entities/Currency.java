package com.codeshare.airline.master.reference.entities;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity(name = "Currency")
@Table(
        name = "CURRENCY",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_CURRENCY", columnNames = "CURRENCY_CODE")
        },
        indexes = {
                @Index(name = "IDX_CURRENCY_STATUS", columnList = "STATUS")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class Currency extends CSMDataAbstractEntity {
    @Column(name = "CURRENCY_CODE", nullable = false, length = 3)
    private String currencyCode;

    @Column(name = "CURRENCY_NAME", nullable = false, length = 100)
    private String currencyName;

    @Column(name = "NUMERIC_CODE", length = 3)
    private String numericCode;


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
        if (currencyCode != null) {
            currencyCode = currencyCode.trim().toUpperCase();
        }

        if (currencyName != null) {
            currencyName = currencyName.trim();
        }

        if (effectiveFrom != null && effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {
            throw new IllegalStateException("Invalid effective period.");
        }
    }
}