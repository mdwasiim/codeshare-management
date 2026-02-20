package com.codeshare.airline.core.dto.codeshare;

import com.codeshare.airline.core.enums.codeshare.CodeshareInventoryType;
import com.codeshare.airline.core.enums.codeshare.CodeshareScopeType;
import com.codeshare.airline.core.enums.codeshare.CodeshareCommercialModel;
import com.codeshare.airline.core.enums.common.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CodeshareAgreementDTO {

    private UUID id;

    private UUID marketingAirlineId;
    private UUID operatingAirlineId;

    private CodeshareCommercialModel codeshareCommercialModel;

    private Status statusCode;
    private CodeshareScopeType scopeType;
    private CodeshareInventoryType inventoryType;

    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}