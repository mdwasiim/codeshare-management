package com.codeshare.airline.tenant.partner.entities;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.core.enums.master.airline.CodeshareAgreementStatus;
import com.codeshare.airline.core.enums.master.airline.CodeshareAgreementType;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "codeshare_partner",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_codeshare_partner", columnNames = {"home_airline_id", "partner_airline_id"})
        },
        indexes = {
                @Index(name = "idx_codeshare_home_airline", columnList = "home_airline_id"),
                @Index(name = "idx_codeshare_partner_airline", columnList = "partner_airline_id"),
                @Index(name = "idx_codeshare_status", columnList = "status"),
                @Index(name = "idx_codeshare_active", columnList = "active")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class CodesharePartner extends CSMDataAbstractEntity {

    @Column(name = "home_airline_id", nullable = false)
    private UUID homeAirlineId;

    @Column(name = "partner_airline_id", nullable = false)
    private UUID partnerAirlineId;

    @Column(name = "agreement_number", length = 50)
    private String agreementNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "agreement_type", nullable = false, length = 30)
    private CodeshareAgreementType agreementType;

    @Enumerated(EnumType.STRING)
    @Column(name = "agreement_status", nullable = false, length = 30)
    private CodeshareAgreementStatus agreementStatus = CodeshareAgreementStatus.ACTIVE;

    @Column(name = "active", nullable = false)
    private Boolean active = Boolean.TRUE;

    @Column(name = "display_order")
    private Integer displayOrder = 1;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "remarks", length = 1000)
    private String remarks;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private RecordStatus recordStatus = RecordStatus.ACTIVE;

    @Column(name = "effective_from")
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;
}
