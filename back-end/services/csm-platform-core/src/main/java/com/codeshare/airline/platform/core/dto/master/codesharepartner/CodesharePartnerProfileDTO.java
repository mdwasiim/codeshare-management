package com.codeshare.airline.platform.core.dto.master.codesharepartner;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.core.enums.master.codesharepartner.CodeshareAgreementCategory;
import com.codeshare.airline.platform.core.enums.master.codesharepartner.InventorySharingType;
import com.codeshare.airline.platform.core.enums.master.codesharepartner.PartnerType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class CodesharePartnerProfileDTO {
    private UUID id;
    private UUID partnerId;
    private String profileCode;
    private String profileName;
    private PartnerType partnerType;
    private CodeshareAgreementCategory agreementCategory;
    private InventorySharingType inventorySharingType;
    private Integer priority;
    private Boolean autoAcceptScheduleChanges;
    private Boolean prorationApplicable;
    private Boolean eTicketAllowed;
    private Boolean active;
    private Integer displayOrder;
    private String description;
    private String remarks;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
