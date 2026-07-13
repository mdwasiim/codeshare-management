package com.codeshare.airline.tenant.entities.partner;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.core.enums.master.airline.CodeshareAgreementStatus;
import com.codeshare.airline.platform.core.enums.master.airline.CodeshareAgreementType;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "codeshare_partner",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_codeshare_partner", columnNames = {"tenant_id", "home_airline_id", "partner_airline_id"})
        },
        indexes = {
                @Index(name = "idx_codeshare_tenant", columnList = "tenant_id"),
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

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(name = "home_airline_id", nullable = false)
    private Long homeAirlineId;

    @Column(name = "partner_airline_id", nullable = false)
    private Long partnerAirlineId;

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
