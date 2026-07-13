package com.codeshare.airline.platform.core.dto.master.codesharepartner;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.core.enums.master.codesharepartner.AuthenticationType;
import com.codeshare.airline.platform.core.enums.master.codesharepartner.CommunicationProtocol;
import com.codeshare.airline.platform.core.enums.master.codesharepartner.MessageFormat;
import com.codeshare.airline.platform.core.enums.master.codesharepartner.TransportType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CodesharePartnerCommunicationProfileDTO {
    private Long id;
    private Long partnerId;
    private String profileCode;
    private String profileName;
    private CommunicationProtocol protocol;
    private TransportType transportType;
    private MessageFormat messageFormat;
    private AuthenticationType authenticationType;
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
