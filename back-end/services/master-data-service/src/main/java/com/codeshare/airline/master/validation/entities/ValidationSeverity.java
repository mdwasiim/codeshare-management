package com.codeshare.airline.master.validation.entities;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity(name = "Validation_Severity")
@Table(
        name = "VALIDATION_SEVERITY",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_VALIDATION_SEVERITY", columnNames = "SEVERITY_CODE")
        },
        indexes = {
                @Index(name = "IDX_VALIDATION_SEVERITY_STATUS", columnList = "STATUS")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ValidationSeverity extends CSMDataAbstractEntity {
    @Column(name = "SEVERITY_CODE", nullable = false, length = 20)
    private String severityCode;

    @Column(name = "SEVERITY_NAME", nullable = false, length = 100)
    private String severityName;

    @Column(name = "SEVERITY_LEVEL")
    private Integer severityLevel;


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
        if (severityCode != null) {
            severityCode = severityCode.trim().toUpperCase();
        }

        if (severityName != null) {
            severityName = severityName.trim();
        }

        if (effectiveFrom != null && effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {
            throw new IllegalStateException("Invalid effective period.");
        }
    }
}