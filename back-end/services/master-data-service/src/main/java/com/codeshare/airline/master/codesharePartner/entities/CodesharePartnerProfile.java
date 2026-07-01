package com.codeshare.airline.master.codesharePartner.entities;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import com.codeshare.airline.master.airline.entities.CodesharePartner;
import com.codeshare.airline.master.codesharePartner.enums.PartnerType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "CODESHARE_PARTNER_PROFILE",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_CODESHARE_PARTNER_PROFILE",
                        columnNames = {
                                "PARTNER_ID",
                                "PROFILE_CODE"
                        }
                )
        },
        indexes = {

                @Index(
                        name = "IDX_CODESHARE_PARTNER_PROFILE_PARTNER",
                        columnList = "PARTNER_ID"
                ),

                @Index(
                        name = "IDX_CODESHARE_PARTNER_PROFILE_STATUS",
                        columnList = "STATUS"
                ),

                @Index(
                        name = "IDX_CODESHARE_PARTNER_PROFILE_ACTIVE",
                        columnList = "ACTIVE"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class CodesharePartnerProfile extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "PARTNER_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_CODESHARE_PARTNER_PROFILE_PARTNER")
    )
    private CodesharePartner partner;

    /**
     * Business Key
     */
    @Column(name = "PROFILE_CODE", nullable = false, length = 30)
    private String profileCode;

    @Column(name = "PROFILE_NAME", nullable = false, length = 150)
    private String profileName;

    /**
     * Operating / Marketing
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "PARTNER_TYPE", nullable = false, length = 30)
    private PartnerType partnerType;

    /**
     * Bilateral / Alliance / Interline
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "AGREEMENT_CATEGORY", nullable = false, length = 30)
    private CodeshareAgreementCategory agreementCategory;

    /**
     * Block Space / Free Sale / Soft Block
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "INVENTORY_SHARING_TYPE", nullable = false, length = 30)
    private InventorySharingType inventorySharingType;

    @Column(name = "PRIORITY")
    private Integer priority = 1;

    @Column(name = "AUTO_ACCEPT_SCHEDULE_CHANGES", nullable = false)
    private Boolean autoAcceptScheduleChanges = Boolean.FALSE;

    @Column(name = "PRORATION_APPLICABLE", nullable = false)
    private Boolean prorationApplicable = Boolean.TRUE;

    @Column(name = "E_TICKET_ALLOWED", nullable = false)
    private Boolean eTicketAllowed = Boolean.TRUE;

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

        if (partner == null) {
            throw new IllegalStateException("Codeshare Partner is mandatory.");
        }

        if (profileCode == null || profileCode.isBlank()) {
            throw new IllegalStateException("Profile Code is mandatory.");
        }

        if (profileName == null || profileName.isBlank()) {
            throw new IllegalStateException("Profile Name is mandatory.");
        }

        if (partnerType == null) {
            throw new IllegalStateException("Partner Type is mandatory.");
        }

        if (agreementCategory == null) {
            throw new IllegalStateException("Agreement Category is mandatory.");
        }

        if (inventorySharingType == null) {
            throw new IllegalStateException("Inventory Sharing Type is mandatory.");
        }

        profileCode = profileCode.trim().toUpperCase();
        profileName = profileName.trim();

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