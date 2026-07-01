package com.codeshare.airline.master.validation.entities;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity(name = "Business_Rule")
@Table(
        name = "BUSINESS_RULE",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_BUSINESS_RULE", columnNames = "RULE_CODE")
        },
        indexes = {
                @Index(name = "IDX_BUSINESS_RULE_STATUS", columnList = "STATUS")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class BusinessRule extends CSMDataAbstractEntity {
    @Column(name = "RULE_CODE", nullable = false, length = 30)
    private String ruleCode;

    @Column(name = "RULE_NAME", nullable = false, length = 150)
    private String ruleName;

    @Column(name = "EXPRESSION", length = 1000)
    private String expression;


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
            name = "VALIDATION_RULE_ID",
            foreignKey = @ForeignKey(name = "FK_BUSINESS_RULE_VALIDATION_RULE")
    )
    private ValidationRule validationRule;
}