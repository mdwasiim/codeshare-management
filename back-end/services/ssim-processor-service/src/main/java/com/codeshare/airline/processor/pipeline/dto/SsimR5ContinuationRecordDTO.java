package com.codeshare.airline.processor.pipeline.dto;

import com.codeshare.airline.core.dto.audit.dto.CSMAuditableDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SsimR5ContinuationRecordDTO extends CSMAuditableDTO {

    private Long id;
    private Long dateVariationId;

    private String recordType;                 // '6'
    private String continuationData;
}
