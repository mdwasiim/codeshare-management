package com.codeshare.airline.platform.core.dto.codeshare;

import com.codeshare.airline.platform.core.enums.codeshare.CodeshareInventoryType;
import com.codeshare.airline.platform.core.enums.codeshare.CodeshareScopeType;
import com.codeshare.airline.platform.core.enums.codeshare.CodeshareCommercialModel;
import com.codeshare.airline.platform.core.enums.common.RecordStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CodeshareAgreementDTO {

    private Long id;

    private Long marketingAirlineId;
    private Long operatingAirlineId;

    private CodeshareCommercialModel codeshareCommercialModel;

    private RecordStatus recordStatus;
    private CodeshareScopeType scopeType;
    private CodeshareInventoryType inventoryType;

    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}