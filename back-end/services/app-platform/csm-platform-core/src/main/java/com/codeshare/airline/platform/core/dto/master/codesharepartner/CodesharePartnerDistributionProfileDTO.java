package com.codeshare.airline.platform.core.dto.master.codesharepartner;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.platform.core.enums.master.codesharepartner.CommunicationProtocol;
import com.codeshare.airline.platform.core.enums.master.codesharepartner.DistributionMode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CodesharePartnerDistributionProfileDTO {
    private Long id;
    private Long partnerId;
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
