package com.codeshare.airline.master.airline.entities;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import com.codeshare.airline.master.airline.entities.enums.AirlineRoleCategory;
import com.codeshare.airline.master.airline.entities.enums.AirlineRoleScope;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "AIRLINE_BUSINESS_ROLE",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_AIRLINE_BUSINESS_ROLE",
                        columnNames = {
                                "AIRLINE_ID",
                                "ROLE_CODE"
                        }
                )
        },
        indexes = {

                @Index(
                        name = "IDX_AIRLINE_BUSINESS_ROLE_AIRLINE",
                        columnList = "AIRLINE_ID"
                ),

                @Index(
                        name = "IDX_AIRLINE_BUSINESS_ROLE_STATUS",
                        columnList = "STATUS"
                ),

                @Index(
                        name = "IDX_AIRLINE_BUSINESS_ROLE_ACTIVE",
                        columnList = "ACTIVE"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class AirlineBusinessRole extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "AIRLINE_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_AIRLINE_BUSINESS_ROLE_AIRLINE")
    )
    private Airline airline;

    @Column(name = "ROLE_CODE", nullable = false, length = 30)
    private String roleCode;

    @Column(name = "ROLE_NAME", nullable = false, length = 150)
    private String roleName;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE_SCOPE", nullable = false, length = 30)
    private AirlineRoleScope roleScope;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE_CATEGORY", nullable = false, length = 30)
    private AirlineRoleCategory roleCategory;

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

        if (roleCode == null || roleCode.isBlank()) {
            throw new IllegalStateException("Role Code is mandatory.");
        }

        if (roleName == null || roleName.isBlank()) {
            throw new IllegalStateException("Role Name is mandatory.");
        }

        if (roleScope == null) {
            throw new IllegalStateException("Role Scope is mandatory.");
        }

        if (roleCategory == null) {
            throw new IllegalStateException("Role Category is mandatory.");
        }

        roleCode = roleCode.trim().toUpperCase();
        roleName = roleName.trim();

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