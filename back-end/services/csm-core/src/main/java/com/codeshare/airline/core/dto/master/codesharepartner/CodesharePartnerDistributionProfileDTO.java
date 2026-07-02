package com.codeshare.airline.core.dto.master.codesharepartner;

import com.codeshare.airline.core.enums.common.RecordStatus;
import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.core.enums.master.codesharepartner.CommunicationProtocol;
import com.codeshare.airline.core.enums.master.codesharepartner.DistributionMode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class CodesharePartnerDistributionProfileDTO {
    private UUID id;
    private UUID partnerId;
    private String profileCode;
    private String profileName;
    private CommunicationProtocol distributionChannel;
    private DistributionMode distributionMode;
    private MessageType messageType;
    private Boolean realTimeEnabled;
    private Boolean acknowledgementRequired;
    private Boolean retryEnabled;
    private Integer retryCount;
    private Boolean active;
    private Integer displayOrder;
    private String description;
    private String remarks;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
