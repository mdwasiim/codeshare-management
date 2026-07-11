package com.codeshare.airline.master.terminal.entities;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import com.codeshare.airline.master.geography.entities.Timezone;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity(name = "Daylight_Saving_Rule")
@Table(
        name = "DAYLIGHT_SAVING_RULE",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_DAYLIGHT_SAVING_RULE", columnNames = "RULE_CODE")
        },
        indexes = {
                @Index(name = "IDX_DAYLIGHT_SAVING_RULE_STATUS", columnList = "STATUS")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class DaylightSavingRule extends CSMDataAbstractEntity {
    @Column(name = "RULE_CODE", nullable = false, length = 30)
    private String ruleCode;

    @Column(name = "RULE_NAME", nullable = false, length = 150)
    private String ruleName;

    @Column(name = "DST_OFFSET_MINUTES")
    private Integer dstOffsetMinutes;

    @Column(name = "START_RULE", length = 100)
    private String startRule;

    @Column(name = "END_RULE", length = 100)
    private String endRule;


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
            name = "TIMEZONE_ID",
            foreignKey = @ForeignKey(name = "FK_DAYLIGHT_SAVING_RULE_TIMEZONE")
    )
    private Timezone timezone;
}