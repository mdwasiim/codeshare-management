package com.codeshare.airline.schedule.ingestion.validation.support;

import com.codeshare.airline.schedule.ingestion.application.validation.MasterDataReferenceValidationPort;
import com.codeshare.airline.schedule.ingestion.domain.enums.ValidationStage;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationResult;
import org.springframework.stereotype.Component;

@Component
public class MasterDataReferenceValidationSupport {

    private final MasterDataReferenceValidationPort referenceValidationPort;

    public MasterDataReferenceValidationSupport(MasterDataReferenceValidationPort referenceValidationPort) {
        this.referenceValidationPort = referenceValidationPort;
    }

    public void validateAirline(ValidationResult result, String airlineCode, String ruleCode, String recordType, String recordKey) {
        if (hasText(airlineCode) && !referenceValidationPort.airlineExists(airlineCode)) {
            result.addError(ruleCode, "Unknown airline reference", recordType, recordKey, ValidationStage.VALIDATION);
        }
    }

    public void validateAirport(ValidationResult result, String airportCode, String ruleCode, String recordType, String recordKey) {
        if (hasText(airportCode) && !referenceValidationPort.airportExists(airportCode)) {
            result.addError(ruleCode, "Unknown airport reference", recordType, recordKey, ValidationStage.VALIDATION);
        }
    }

    public void validateAircraft(ValidationResult result, String aircraftCode, String ruleCode, String recordType, String recordKey) {
        if (hasText(aircraftCode) && !referenceValidationPort.aircraftExists(aircraftCode)) {
            result.addError(ruleCode, "Unknown aircraft reference", recordType, recordKey, ValidationStage.VALIDATION);
        }
    }

    public void validateServiceType(ValidationResult result, String serviceTypeCode, String ruleCode, String recordType, String recordKey) {
        if (hasText(serviceTypeCode) && !referenceValidationPort.serviceTypeExists(serviceTypeCode)) {
            result.addError(ruleCode, "Unknown service type reference", recordType, recordKey, ValidationStage.VALIDATION);
        }
    }

    public void validateTrafficRestriction(ValidationResult result, String restrictionCode, String ruleCode, String recordType, String recordKey) {
        if (hasText(restrictionCode) && !referenceValidationPort.trafficRestrictionExists(restrictionCode)) {
            result.addError(ruleCode, "Unknown traffic restriction reference", recordType, recordKey, ValidationStage.VALIDATION);
        }
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
