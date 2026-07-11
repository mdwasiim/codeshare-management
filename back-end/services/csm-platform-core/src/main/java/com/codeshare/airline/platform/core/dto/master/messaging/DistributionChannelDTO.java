package com.codeshare.airline.platform.core.dto.master.messaging;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class DistributionChannelDTO {

    private UUID id;
    private String channelCode;
    private String channelName;
    private String channelType;
    private String protocolType;
    private String endpointUrl;
    private Boolean autoSend;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}