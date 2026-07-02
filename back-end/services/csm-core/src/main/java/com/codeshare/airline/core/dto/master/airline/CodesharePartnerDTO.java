package com.codeshare.airline.core.dto.master.airline;

import com.codeshare.airline.core.enums.common.RecordStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class CodesharePartnerDTO {
    private UUID id;
    private UUID homeAirlineId;
    private UUID partnerAirlineId;
    private String agreementNumber;
    private String agreementType;
    private String agreementStatus;
    private Boolean active;
    private Integer displayOrder;
    private String description;
    private String remarks;
    private RecordStatus recordStatus;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}
