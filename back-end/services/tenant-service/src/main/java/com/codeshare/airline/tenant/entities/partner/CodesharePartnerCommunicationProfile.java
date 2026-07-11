package com.codeshare.airline.tenant.entities.partner;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.core.enums.master.codesharepartner.AuthenticationType;
import com.codeshare.airline.platform.core.enums.master.codesharepartner.CommunicationProtocol;
import com.codeshare.airline.platform.core.enums.master.codesharepartner.MessageFormat;
import com.codeshare.airline.platform.core.enums.master.codesharepartner.TransportType;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "codeshare_partner_communication_profile")
@Getter
@Setter
@NoArgsConstructor
public class CodesharePartnerCommunicationProfile extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "partner_id", nullable = false)
    private CodesharePartner partner;

    @Column(name = "profile_code", nullable = false, length = 30)
    private String profileCode;

    @Column(name = "profile_name", nullable = false, length = 150)
    private String profileName;

    @Enumerated(EnumType.STRING)
    @Column(name = "protocol", nullable = false, length = 30)
    private CommunicationProtocol protocol;

    @Enumerated(EnumType.STRING)
    @Column(name = "transport_type", nullable = false, length = 30)
    private TransportType transportType;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_format", nullable = false, length = 30)
    private MessageFormat messageFormat;

    @Enumerated(EnumType.STRING)
    @Column(name = "authentication_type", nullable = false, length = 30)
    private AuthenticationType authenticationType;

    @Column(name = "endpoint_url", length = 1000)
    private String endpointUrl;

    @Column(name = "username", length = 255)
    private String username;

    @Column(name = "credential_alias", length = 255)
    private String credentialAlias;

    @Column(name = "connection_timeout")
    private Integer connectionTimeout;

    @Column(name = "read_timeout")
    private Integer readTimeout;

    @Column(name = "retry_count")
    private Integer retryCount;

    @Column(name = "compression_enabled", nullable = false)
    private Boolean compressionEnabled = Boolean.FALSE;

    @Column(name = "encryption_enabled", nullable = false)
    private Boolean encryptionEnabled = Boolean.FALSE;

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
