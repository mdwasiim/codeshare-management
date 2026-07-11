package com.codeshare.airline.platform.core.dto.master.airline;

import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import com.codeshare.airline.platform.core.enums.master.airline.CodeshareAgreementStatus;
import com.codeshare.airline.platform.core.enums.master.airline.CodeshareAgreementType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class CodesharePartnerDTO {
    private UUID id;
    private UUID tenantId;
    private UUID homeAirlineId;
    private UUID partnerAirlineId;
    private String homeAirlineCode;
    private String homeAirlineName;
    private String partnerAirlineCode;
    private String partnerAirlineName;
    private String agreementNumber;
    private CodeshareAgreementType agreementType;
    private CodeshareAgreementStatus agreementStatus;
    private Boolean active;
    private Integer displayOrder;
    private String description;
    private String remarks;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
