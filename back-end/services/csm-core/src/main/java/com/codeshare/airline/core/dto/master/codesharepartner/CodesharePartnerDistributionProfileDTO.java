package com.codeshare.airline.core.dto.master.codesharepartner;

import com.codeshare.airline.core.enums.common.RecordStatus;
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
    private String distributionChannel;
    private String distributionMode;
    private String messageType;
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
