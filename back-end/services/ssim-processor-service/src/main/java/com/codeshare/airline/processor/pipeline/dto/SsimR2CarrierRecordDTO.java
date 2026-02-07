package com.codeshare.airline.processor.pipeline.dto;

import com.codeshare.airline.core.dto.audit.dto.CSMAuditableDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SsimR2CarrierRecordDTO extends CSMAuditableDTO {

    private Long id;
    private Long datasetId;

    private String recordType;                 // '2'
    private String airlineDesignator;
    private String airlineNumericCode;
    private String airlineName;

    private String countryCode;
    private String currencyCode;
    private String icaoDesignator;

    private String duplicateDesignatorMarker;
    private String remarks;
}
