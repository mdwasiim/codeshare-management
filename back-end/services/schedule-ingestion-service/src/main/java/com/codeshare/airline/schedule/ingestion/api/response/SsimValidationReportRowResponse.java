package com.codeshare.airline.schedule.ingestion.api.response;

import com.codeshare.airline.schedule.ingestion.domain.enums.ValidationStage;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationSeverity;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class SsimValidationReportRowResponse {
    private String recordType;
    private String recordKey;
    private String ruleCode;
    private String message;
    private ValidationSeverity severity;
    private ValidationStage validationStage;
    private Instant validatedAt;
    private String ruleVersion;
}
