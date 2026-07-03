package com.codeshare.airline.master.airlines.codesharepartner.entities;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import com.codeshare.airline.master.airlines.entities.CodesharePartner;
import com.codeshare.airline.core.enums.master.codesharepartner.DistributionMode;
import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.core.enums.master.codesharepartner.CommunicationProtocol;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "CODESHARE_PARTNER_DISTRIBUTION_PROFILE",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_CODESHARE_PARTNER_DISTRIBUTION_PROFILE",
                        columnNames = {
                                "PARTNER_ID",
                                "PROFILE_CODE"
                        }
                )
        },
        indexes = {

                @Index(
                        name = "IDX_CODESHARE_DIST_PROFILE_PARTNER",
                        columnList = "PARTNER_ID"
                ),

                @Index(
                        name = "IDX_CODESHARE_DIST_PROFILE_STATUS",
                        columnList = "STATUS"
                ),

                @Index(
                        name = "IDX_CODESHARE_DIST_PROFILE_ACTIVE",
                        columnList = "ACTIVE"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class CodesharePartnerDistributionProfile extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "PARTNER_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_CODESHARE_DIST_PROFILE_PARTNER")
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
     * API / MQ / SFTP / EMAIL
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "DISTRIBUTION_CHANNEL", nullable = false, length = 30)
    private CommunicationProtocol distributionChannel;

    /**
     * REAL_TIME / SCHEDULED / MANUAL
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "DISTRIBUTION_MODE", nullable = false, length = 30)
    private DistributionMode distributionMode;

    /**
     * SSIM / ASM / SSM
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "MESSAGE_TYPE", nullable = false, length = 30)
    private MessageType messageType;

    @Column(name = "REAL_TIME_ENABLED", nullable = false)
    private Boolean realTimeEnabled = Boolean.TRUE;

    @Column(name = "ACK_REQUIRED", nullable = false)
    private Boolean acknowledgementRequired = Boolean.TRUE;

    @Column(name = "RETRY_ENABLED", nullable = false)
    private Boolean retryEnabled = Boolean.TRUE;

    @Column(name = "RETRY_COUNT")
    private Integer retryCount = 3;

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

        if (distributionChannel == null) {
            throw new IllegalStateException("Distribution Channel is mandatory.");
        }

        if (distributionMode == null) {
            throw new IllegalStateException("Distribution Mode is mandatory.");
        }

        if (messageType == null) {
            throw new IllegalStateException("Message Type is mandatory.");
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
