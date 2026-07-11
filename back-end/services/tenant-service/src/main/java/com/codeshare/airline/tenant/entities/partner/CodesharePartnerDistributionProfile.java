package com.codeshare.airline.tenant.entities.partner;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.core.enums.master.codesharepartner.CommunicationProtocol;
import com.codeshare.airline.platform.core.enums.master.codesharepartner.DistributionMode;
import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.platform.data.jpa.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "codeshare_partner_distribution_profile")
@Getter
@Setter
@NoArgsConstructor
public class CodesharePartnerDistributionProfile extends CSMDataAbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "partner_id", nullable = false)
    private CodesharePartner partner;

    @Column(name = "profile_code", nullable = false, length = 30)
    private String profileCode;

    @Column(name = "profile_name", nullable = false, length = 150)
    private String profileName;

    @Enumerated(EnumType.STRING)
    @Column(name = "distribution_channel", nullable = false, length = 30)
    private CommunicationProtocol distributionChannel;

    @Enumerated(EnumType.STRING)
    @Column(name = "distribution_mode", nullable = false, length = 30)
    private DistributionMode distributionMode;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", nullable = false, length = 30)
    private MessageType messageType;

    @Column(name = "real_time_enabled", nullable = false)
    private Boolean realTimeEnabled = Boolean.FALSE;

    @Column(name = "acknowledgement_required", nullable = false)
    private Boolean acknowledgementRequired = Boolean.FALSE;

    @Column(name = "retry_enabled", nullable = false)
    private Boolean retryEnabled = Boolean.FALSE;

    @Column(name = "retry_count")
    private Integer retryCount;

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
