package com.codeshare.airline.data.codeshare.eitities;

import com.codeshare.airline.core.enums.codeshare.DeiRuleType;
import com.codeshare.airline.core.enums.common.Status;
import com.codeshare.airline.data.ssim.eitities.Dei;
import com.codeshare.airline.persistence.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "MASTER_CODESHARE_DEI_RULE",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_CS_DEI_MAPPING",
                        columnNames = {
                                "FLIGHT_MAPPING_ID",
                                "DEI_ID",
                                "EFFECTIVE_FROM"
                        }
                )
        },
        indexes = {
                @Index(name = "IDX_CS_DEI_MAPPING", columnList = "FLIGHT_MAPPING_ID"),
                @Index(name = "IDX_CS_DEI_STATUS", columnList = "STATUS_CODE")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class CodeshareDeiRule extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "FLIGHT_MAPPING_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_CS_DEI_RULE_MAPPING")
    )
    private CodeshareFlightMapping flightMapping;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "DEI_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_CS_DEI_MASTER")
    )
    private Dei dei;

    // Value to inject or enforce
    @Column(name = "DEI_VALUE", length = 500)
    private String deiValue;

    // If true → record invalid if DEI missing
    @Column(name = "IS_MANDATORY", nullable = false)
    private Boolean mandatory = Boolean.FALSE;

    // If true → overwrite existing DEI value
    @Column(name = "OVERRIDE_EXISTING", nullable = false)
    private Boolean overrideExisting = Boolean.FALSE;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS_CODE", nullable = false, length = 20)
    private Status statusCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "DEI_RULE_TYPE", nullable = false, length = 20)
    private DeiRuleType deiRuleType;

    @Column(name = "EFFECTIVE_FROM", nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "EFFECTIVE_TO")
    private LocalDate effectiveTo;

    @Column(name = "RULE_PRIORITY", nullable = false)
    private Integer priority;


    @PrePersist
    @PreUpdate
    private void validate() {

        if (effectiveFrom != null && effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {
            throw new IllegalStateException("Invalid effective period.");
        }

        if (priority == null || priority <= 0) {
            throw new IllegalStateException("Priority must be greater than zero.");
        }

        // Override requires a value
        if (Boolean.TRUE.equals(overrideExisting) &&
                (deiValue == null || deiValue.isBlank())) {
            throw new IllegalStateException(
                    "Override requires a DEI value."
            );
        }

        // Prevent no-op rule
        if (!Boolean.TRUE.equals(mandatory) &&
                !Boolean.TRUE.equals(overrideExisting) &&
                (deiValue == null || deiValue.isBlank())) {
            throw new IllegalStateException("Rule has no effect.");
        }
    }
}
