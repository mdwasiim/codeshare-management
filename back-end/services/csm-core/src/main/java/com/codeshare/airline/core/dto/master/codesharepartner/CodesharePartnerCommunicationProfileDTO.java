package com.codeshare.airline.core.dto.master.codesharepartner;

import com.codeshare.airline.core.enums.common.RecordStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class CodesharePartnerCommunicationProfileDTO {
    private UUID id;
    private UUID partnerId;
    private String profileCode;
    private String profileName;
    private String protocol;
    private String transportType;
    private String messageFormat;
    private String authenticationType;
    private String endpointUrl;
    private String username;
    private String credentialAlias;
    private Integer connectionTimeout;
    private Integer readTimeout;
    private Integer retryCount;
    private Boolean compressionEnabled;
    private Boolean encryptionEnabled;
    private Boolean active;
    private Integer displayOrder;
    private String description;
    private String remarks;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
