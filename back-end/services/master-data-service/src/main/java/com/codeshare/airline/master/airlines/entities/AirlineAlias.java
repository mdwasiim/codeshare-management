package com.codeshare.airline.master.airlines.entities;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import com.codeshare.airline.platform.core.enums.master.airline.AirlineAliasType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "AIRLINE_ALIAS",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_AIRLINE_ALIAS",
                        columnNames = {
                                "AIRLINE_ID",
                                "ALIAS_CODE"
                        }
                )
        },
        indexes = {
                @Index(
                        name = "IDX_AIRLINE_ALIAS_AIRLINE",
                        columnList = "AIRLINE_ID"
                ),
                @Index(
                        name = "IDX_AIRLINE_ALIAS_STATUS",
                        columnList = "STATUS"
                ),
                @Index(
                        name = "IDX_AIRLINE_ALIAS_ACTIVE",
                        columnList = "ACTIVE"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class AirlineAlias extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "AIRLINE_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_AIRLINE_ALIAS_AIRLINE")
    )
    private AirlineCarrier airline;

    /**
     * Business Key
     */
    @Column(name = "ALIAS_CODE", nullable = false, length = 30)
    private String aliasCode;

    /**
     * Example:
     * Qatar Airways
     * Qatar Airways Cargo
     * Ø§Ù„Ù‚Ø·Ø±ÙŠØ©
     */
    @Column(name = "ALIAS_NAME", nullable = false, length = 200)
    private String aliasName;

    @Enumerated(EnumType.STRING)
    @Column(name = "ALIAS_TYPE", nullable = false, length = 30)
    private AirlineAliasType aliasType;

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

        if (airline == null) {
            throw new IllegalStateException("Airline is mandatory.");
        }

        if (aliasCode == null || aliasCode.isBlank()) {
            throw new IllegalStateException("Alias Code is mandatory.");
        }

        if (aliasName == null || aliasName.isBlank()) {
            throw new IllegalStateException("Alias Name is mandatory.");
        }

        if (aliasType == null) {
            throw new IllegalStateException("Alias Type is mandatory.");
        }

        aliasCode = aliasCode.trim().toUpperCase();
        aliasName = aliasName.trim();

        if (description != null) {
            description = description.trim();
        }

        if (remarks != null) {
            remarks = remarks.trim();
        }

        if (effectiveFrom != null &&
                effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {
            throw new IllegalStateException("Effective From cannot be after Effective To.");
        }
    }
}