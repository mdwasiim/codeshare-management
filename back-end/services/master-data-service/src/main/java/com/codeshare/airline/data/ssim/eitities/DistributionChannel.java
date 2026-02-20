package com.codeshare.airline.data.ssim.eitities;

import com.codeshare.airline.core.enums.common.Status;
import com.codeshare.airline.persistence.persistence.entity.CSMDataAbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "MASTER_DISTRIBUTION_CHANNEL",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_DISTRIBUTION_CHANNEL_CODE", columnNames = "CHANNEL_CODE")
        },
        indexes = {
                @Index(name = "IDX_DISTRIBUTION_CHANNEL_CODE", columnList = "CHANNEL_CODE"),
                @Index(name = "IDX_DISTRIBUTION_CHANNEL_STATUS", columnList = "STATUS_CODE")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class DistributionChannel extends CSMDataAbstractEntity {

    @Column(name = "CHANNEL_CODE", nullable = false, length = 20)
    private String channelCode;   // 1A, 1G, SITA, API

    @Column(name = "CHANNEL_NAME", nullable = false, length = 100)
    private String channelName;

    @Column(name = "CHANNEL_TYPE", nullable = false, length = 30)
    private String channelType;   // GDS, SITA, API, INTERNAL

    @Column(name = "PROTOCOL_TYPE", length = 30)
    private String protocolType;  // EDIFACT, XML, MQ, REST

    @Column(name = "ENDPOINT_URL", length = 255)
    private String endpointUrl;

    @Column(name = "AUTO_SEND", nullable = false)
    private Boolean autoSend = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS_CODE", nullable = false, length = 20)
    private Status statusCode;

    @Column(name = "EFFECTIVE_FROM")
    private LocalDate effectiveFrom;

    @Column(name = "EFFECTIVE_TO")
    private LocalDate effectiveTo;

    @PrePersist
    @PreUpdate
    private void normalize() {
        if (channelCode != null) {
            channelCode = channelCode.toUpperCase();
        }
        if (channelType != null) {
            channelType = channelType.toUpperCase();
        }
        if (protocolType != null) {
            protocolType = protocolType.toUpperCase();
        }
    }
}
