package com.codeshare.airline.platform.core.dto.master.codesharepartner;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.core.enums.schedule.ApprovalMode;
import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CodesharePartnerAcceptanceRuleDTO {
    private Long id;
    private Long partnerId;
    private String tenantCode;
    private String partnerCode;
    private MessageType messageType;
    private ApprovalMode approvalMode;
    private Boolean active;
    private Integer displayOrder;
    private String description;
    private String remarks;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
