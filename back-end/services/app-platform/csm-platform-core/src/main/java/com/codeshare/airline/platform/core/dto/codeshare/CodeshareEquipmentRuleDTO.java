package com.codeshare.airline.platform.core.dto.codeshare;

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
public class CodeshareEquipmentRuleDTO {

    private Long id;

    private Long flightMappingId;
    private Long aircraftTypeId;

    private RecordStatus recordStatus;

    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
}