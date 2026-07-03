package com.codeshare.airline.master.airlines.codesharepartner.entities;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.data.entity.CSMDataAbstractEntity;
import com.codeshare.airline.master.airlines.entities.CodesharePartner;
import com.codeshare.airline.core.enums.master.codesharepartner.AuthenticationType;
import com.codeshare.airline.core.enums.master.codesharepartner.CommunicationProtocol;
import com.codeshare.airline.core.enums.master.codesharepartner.TransportType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.codeshare.airline.core.enums.master.codesharepartner.MessageFormat;
import java.time.LocalDate;

@Entity
@Table(
        name = "CODESHARE_PARTNER_COMMUNICATION_PROFILE",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_CODESHARE_PARTNER_COMM_PROFILE",
                        columnNames = {
                                "PARTNER_ID",
                                "PROFILE_CODE"
                        }
                )
        },
        indexes = {

                @Index(
                        name = "IDX_CODESHARE_COMM_PROFILE_PARTNER",
                        columnList = "PARTNER_ID"
                ),

                @Index(
                        name = "IDX_CODESHARE_COMM_PROFILE_STATUS",
                        columnList = "STATUS"
                ),

                @Index(
                        name = "IDX_CODESHARE_COMM_PROFILE_ACTIVE",
                        columnList = "ACTIVE"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class CodesharePartnerCommunicationProfile extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "PARTNER_ID",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_CODESHARE_COMM_PROFILE_PARTNER")
    )
    private CodesharePartner partner;

    @Column(name = "PROFILE_CODE", nullable = false, length = 30)
    private String profileCode;

    @Column(name = "PROFILE_NAME", nullable = false, length = 150)
    private String profileName;

    @Enumerated(EnumType.STRING)
    @Column(name = "PROTOCOL", nullable = false, length = 30)
    private CommunicationProtocol protocol;

    @Enumerated(EnumType.STRING)
    @Column(name = "TRANSPORT_TYPE", nullable = false, length = 30)
    private TransportType transportType;

    @Enumerated(EnumType.STRING)
    @Column(name = "MESSAGE_FORMAT", nullable = false, length = 30)
    private MessageFormat messageFormat;

    @Enumerated(EnumType.STRING)
    @Column(name = "AUTHENTICATION_TYPE", nullable = false, length = 30)
    private AuthenticationType authenticationType;

    @Column(name = "ENDPOINT_URL", length = 500)
    private String endpointUrl;

    @Column(name = "USERNAME", length = 100)
    private String username;

    /**
     * Secret Manager / Vault Alias
     */
    @Column(name = "CREDENTIAL_ALIAS", length = 100)
    private String credentialAlias;

    @Column(name = "CONNECTION_TIMEOUT")
    private Integer connectionTimeout = 30000;

    @Column(name = "READ_TIMEOUT")
    private Integer readTimeout = 30000;

    @Column(name = "RETRY_COUNT")
    private Integer retryCount = 3;

    @Column(name = "COMPRESSION_ENABLED", nullable = false)
    private Boolean compressionEnabled = Boolean.FALSE;

    @Column(name = "ENCRYPTION_ENABLED", nullable = false)
    private Boolean encryptionEnabled = Boolean.TRUE;

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

        if (protocol == null) {
            throw new IllegalStateException("Protocol is mandatory.");
        }

        if (transportType == null) {
            throw new IllegalStateException("Transport Type is mandatory.");
        }

        if (messageFormat == null) {
            throw new IllegalStateException("Message Format is mandatory.");
        }

        if (authenticationType == null) {
            throw new IllegalStateException("Authentication Type is mandatory.");
        }

        profileCode = profileCode.trim().toUpperCase();
        profileName = profileName.trim();

        if (endpointUrl != null) {
            endpointUrl = endpointUrl.trim();
        }

        if (username != null) {
            username = username.trim();
        }

        if (credentialAlias != null) {
            credentialAlias = credentialAlias.trim();
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
