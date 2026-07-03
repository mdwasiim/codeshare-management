package com.codeshare.airline.master.airlines.entities;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import com.codeshare.airline.core.enums.master.airline.CodeshareAgreementStatus;
import com.codeshare.airline.core.enums.master.airline.CodeshareAgreementType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "CODESHARE_PARTNER",
        uniqueConstraints = {

                @UniqueConstraint(
                        name = "UK_CODESHARE_PARTNER",
                        columnNames = {
                                "HOME_AIRLINE_ID",
                                "PARTNER_AIRLINE_ID"
                        }
                )
        },
        indexes = {

                @Index(
                        name = "IDX_CODESHARE_HOME_AIRLINE",
                        columnList = "HOME_AIRLINE_ID"
                ),

                @Index(
                        name = "IDX_CODESHARE_PARTNER_AIRLINE",
                        columnList = "PARTNER_AIRLINE_ID"
                ),

                @Index(
                        name = "IDX_CODESHARE_STATUS",
                        columnList = "STATUS"
                ),

                @Index(
                        name = "IDX_CODESHARE_ACTIVE",
                        columnList = "ACTIVE"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class CodesharePartner extends CSMDataAbstractEntity {

    /**
     * Airline owning this agreement.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "HOME_AIRLINE_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_CODESHARE_HOME_AIRLINE")
    )
    private AirlineCarrier homeAirline;

    /**
     * Partner airline.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "PARTNER_AIRLINE_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_CODESHARE_PARTNER_AIRLINE")
    )
    private AirlineCarrier partnerAirline;

    /**
     * Commercial agreement reference.
     */
    @Column(name = "AGREEMENT_NUMBER", length = 50)
    private String agreementNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "AGREEMENT_TYPE", nullable = false, length = 30)
    private CodeshareAgreementType agreementType;

    @Enumerated(EnumType.STRING)
    @Column(name = "AGREEMENT_STATUS", nullable = false, length = 30)
    private CodeshareAgreementStatus agreementStatus =
            CodeshareAgreementStatus.ACTIVE;

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

        if (homeAirline == null) {
            throw new IllegalStateException("Home Airline is mandatory.");
        }

        if (partnerAirline == null) {
            throw new IllegalStateException("Partner Airline is mandatory.");
        }

        if (homeAirline.equals(partnerAirline)) {
            throw new IllegalStateException(
                    "Home Airline and Partner Airline cannot be the same."
            );
        }

        if (agreementType == null) {
            throw new IllegalStateException("Agreement Type is mandatory.");
        }

        if (agreementStatus == null) {
            throw new IllegalStateException("Agreement Status is mandatory.");
        }

        if (agreementNumber != null) {
            agreementNumber = agreementNumber.trim().toUpperCase();
        }

        if (description != null) {
            description = description.trim();
        }

        if (remarks != null) {
            remarks = remarks.trim();
        }

        if (effectiveFrom != null &&
                effectiveTo != null &&
                effectiveFrom.isAfter(effectiveTo)) {

            throw new IllegalStateException(
                    "Effective From cannot be after Effective To."
            );
        }
    }
}