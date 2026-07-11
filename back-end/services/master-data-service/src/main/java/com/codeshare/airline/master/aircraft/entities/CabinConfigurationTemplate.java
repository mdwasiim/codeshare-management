package com.codeshare.airline.master.aircraft.entities;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import com.codeshare.airline.platform.core.enums.master.aircraft.CabinConfigurationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "CABIN_CONFIGURATION",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_CABIN_CONFIGURATION_CODE",
                        columnNames = "CABIN_CONFIGURATION_CODE"
                )
        },
        indexes = {
                @Index(
                        name = "IDX_CABIN_CONFIGURATION_STATUS",
                        columnList = "STATUS"
                ),
                @Index(
                        name = "IDX_CABIN_CONFIGURATION_ACTIVE",
                        columnList = "ACTIVE"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class CabinConfigurationTemplate extends CSMDataAbstractEntity {

    @Column(name = "CABIN_CONFIGURATION_CODE",
            nullable = false,
            length = 20)
    private String configurationCode;

    @Column(name = "CABIN_CONFIGURATION_NAME",
            nullable = false,
            length = 150)
    private String configurationName;

    /**
     * Domestic
     * International
     * High Density
     * Low Density
     * VIP
     * Cargo
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "CONFIGURATION_TYPE",
            nullable = false,
            length = 30)
    private CabinConfigurationType configurationType;

    @Column(name = "ACTIVE",
            nullable = false)
    private Boolean active = Boolean.TRUE;

    @Column(name = "DISPLAY_ORDER")
    private Integer displayOrder = 1;

    @Column(name = "DESCRIPTION",
            length = 500)
    private String description;

    @Column(name = "REMARKS",
            length = 1000)
    private String remarks;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS",
            nullable = false,
            length = 20)
    private RecordStatus recordStatus = RecordStatus.ACTIVE;

    @Column(name = "EFFECTIVE_FROM")
    private LocalDate effectiveFrom;

    @Column(name = "EFFECTIVE_TO")
    private LocalDate effectiveTo;

    @PrePersist
    @PreUpdate
    private void normalizeAndValidate() {

        if (configurationCode == null || configurationCode.isBlank()) {
            throw new IllegalStateException(
                    "Configuration Code is mandatory.");
        }

        if (configurationName == null || configurationName.isBlank()) {
            throw new IllegalStateException(
                    "Configuration Name is mandatory.");
        }

        if (configurationType == null) {
            throw new IllegalStateException(
                    "Configuration Type is mandatory.");
        }

        configurationCode =
                configurationCode.trim().toUpperCase();

        configurationName =
                configurationName.trim();

        if (effectiveFrom != null &&
                effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {

            throw new IllegalStateException(
                    "Effective From cannot be after Effective To.");
        }
    }
}