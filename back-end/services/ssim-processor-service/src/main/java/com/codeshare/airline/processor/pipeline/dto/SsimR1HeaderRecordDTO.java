package com.codeshare.airline.processor.pipeline.dto;

import com.codeshare.airline.core.dto.audit.dto.CSMAuditableDTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SsimR1HeaderRecordDTO extends CSMAuditableDTO {

    private Long id;
    private Long datasetId;

    private String recordType;                 // '1'
    private String airlineDesignator;
    private String datasetSerialNumber;

    private String creationDate;               // YYMMDD
    private String creationTime;               // HHMM

    private String scheduleType;
    private String periodStartDate;
    private String periodEndDate;

    private String versionNumber;
    private String continuationIndicator;
    private String generalInformation;
}

