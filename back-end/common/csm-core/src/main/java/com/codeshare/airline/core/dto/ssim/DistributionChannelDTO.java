package com.codeshare.airline.core.dto.ssim;

import com.codeshare.airline.core.enums.common.Status;
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
    private Status statusCode;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}