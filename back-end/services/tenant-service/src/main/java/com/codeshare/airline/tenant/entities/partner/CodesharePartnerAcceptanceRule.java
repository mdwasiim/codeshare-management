package com.codeshare.airline.tenant.entities.partner;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.core.enums.schedule.ApprovalMode;
import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "codeshare_partner_acceptance_rule",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_partner_acceptance_msg", columnNames = {"partner_id", "message_type"})
        },
        indexes = {
                @Index(name = "idx_partner_acceptance_partner", columnList = "partner_id"),
                @Index(name = "idx_partner_acceptance_message", columnList = "message_type"),
                @Index(name = "idx_partner_acceptance_active", columnList = "active")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class CodesharePartnerAcceptanceRule extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "partner_id", nullable = false)
    private CodesharePartner partner;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", nullable = false, length = 30)
    private MessageType messageType;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_mode", nullable = false, length = 20)
    private ApprovalMode approvalMode = ApprovalMode.MANUAL;

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
