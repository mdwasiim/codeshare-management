package com.codeshare.airline.tenant.entities.partner;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.core.enums.master.codesharepartner.CodeshareAgreementCategory;
import com.codeshare.airline.platform.core.enums.master.codesharepartner.InventorySharingType;
import com.codeshare.airline.platform.core.enums.master.codesharepartner.PartnerType;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "codeshare_partner_profile")
@Getter
@Setter
@NoArgsConstructor
public class CodesharePartnerProfile extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "partner_id", nullable = false)
    private CodesharePartner partner;

    @Column(name = "profile_code", nullable = false, length = 30)
    private String profileCode;

    @Column(name = "profile_name", nullable = false, length = 150)
    private String profileName;

    @Enumerated(EnumType.STRING)
    @Column(name = "partner_type", nullable = false, length = 30)
    private PartnerType partnerType;

    @Enumerated(EnumType.STRING)
    @Column(name = "agreement_category", nullable = false, length = 30)
    private CodeshareAgreementCategory agreementCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "inventory_sharing_type", nullable = false, length = 30)
    private InventorySharingType inventorySharingType;

    @Column(name = "priority")
    private Integer priority = 1;

    @Column(name = "auto_accept_schedule_changes", nullable = false)
    private Boolean autoAcceptScheduleChanges = Boolean.FALSE;

    @Column(name = "proration_applicable", nullable = false)
    private Boolean prorationApplicable = Boolean.TRUE;

    @Column(name = "e_ticket_allowed", nullable = false)
    private Boolean eTicketAllowed = Boolean.TRUE;

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
