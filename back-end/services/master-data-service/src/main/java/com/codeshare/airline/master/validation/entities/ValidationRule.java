package com.codeshare.airline.master.validation.entities;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity(name = "Validation_Rule")
@Table(
        name = "VALIDATION_RULE",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_VALIDATION_RULE", columnNames = "RULE_CODE")
        },
        indexes = {
                @Index(name = "IDX_VALIDATION_RULE_STATUS", columnList = "STATUS")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class ValidationRule extends CSMDataAbstractEntity {
    @Column(name = "RULE_CODE", nullable = false, length = 30)
    private String ruleCode;

    @Column(name = "RULE_NAME", nullable = false, length = 150)
    private String ruleName;

    @Column(name = "RULE_EXPRESSION", length = 1000)
    private String ruleExpression;


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
        if (ruleCode != null) {
            ruleCode = ruleCode.trim().toUpperCase();
        }

        if (ruleName != null) {
            ruleName = ruleName.trim();
        }

        if (effectiveFrom != null && effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {
            throw new IllegalStateException("Invalid effective period.");
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "VALIDATION_CATEGORY_ID",
            foreignKey = @ForeignKey(name = "FK_VALIDATION_RULE_VALIDATION_CATEGORY")
    )
    private ValidationCategory validationCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "VALIDATION_SEVERITY_ID",
            foreignKey = @ForeignKey(name = "FK_VALIDATION_RULE_VALIDATION_SEVERITY")
    )
    private ValidationSeverity validationSeverity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "ERROR_CODE_ID",
            foreignKey = @ForeignKey(name = "FK_VALIDATION_RULE_ERROR_CODE")
    )
    private ErrorCode errorCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "WARNING_CODE_ID",
            foreignKey = @ForeignKey(name = "FK_VALIDATION_RULE_WARNING_CODE")
    )
    private WarningCode warningCode;
}