package com.codeshare.airline.schedule.ingestion.validation.orchestrator;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.context.AbstractIngestionContext;
import com.codeshare.airline.schedule.ingestion.validation.engine.ValidationEngine;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationResult;
import com.codeshare.airline.schedule.ingestion.validation.validator.BusinessValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BusinessValidationOrchestrator {

    private final List<BusinessValidation<?>> validators;

    private final ValidationEngine validationEngine;

    public ValidationResult validate(AbstractIngestionContext<?, ?> context) {
        return validateByType(context.getMessageType(), context);
    }

    private ValidationResult validateByType(MessageType type, AbstractIngestionContext<?, ?> context) {
        List<BusinessValidation<?>> matchingValidators = validators.stream()
                .filter(validator -> validator.supportedTypes().contains(type))
                .toList();

        return validationEngine.validate(
                matchingValidators,
                context,
                BusinessValidation::validateContext,
                false
        );
    }
}
