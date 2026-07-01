package com.codeshare.airline.master.schedule.entities;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity(name = "Operational_Suffix")
@Table(
        name = "OPERATIONAL_SUFFIX",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_OPERATIONAL_SUFFIX", columnNames = "SUFFIX_CODE")
        },
        indexes = {
                @Index(name = "IDX_OPERATIONAL_SUFFIX_STATUS", columnList = "STATUS")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class OperationalSuffix extends CSMDataAbstractEntity {
    @Column(name = "SUFFIX_CODE", nullable = false, length = 1)
    private String suffixCode;

    @Column(name = "SUFFIX_NAME", nullable = false, length = 100)
    private String suffixName;


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
        if (suffixCode != null) {
            suffixCode = suffixCode.trim().toUpperCase();
        }

        if (suffixName != null) {
            suffixName = suffixName.trim();
        }

        if (effectiveFrom != null && effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {
            throw new IllegalStateException("Invalid effective period.");
        }
    }
}