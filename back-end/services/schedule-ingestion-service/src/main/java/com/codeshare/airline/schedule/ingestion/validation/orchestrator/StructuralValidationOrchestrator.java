package com.codeshare.airline.schedule.ingestion.validation.orchestrator;

import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.context.AbstractIngestionContext;
import com.codeshare.airline.schedule.ingestion.validation.engine.ValidationEngine;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationResult;
import com.codeshare.airline.schedule.ingestion.validation.validator.StructuralValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StructuralValidationOrchestrator {

    private final List<StructuralValidation<?>> validators;

    private final ValidationEngine validationEngine;

    public ValidationResult validate(AbstractIngestionContext<?, ?> context) {
        return validateByType(context.getMessageType(), context);
    }

    private ValidationResult validateByType(MessageType type, AbstractIngestionContext<?, ?> context) {
        List<StructuralValidation<?>> matchingValidators = validators.stream()
                .filter(validator -> validator.supportedTypes().contains(type))
                .toList();

        return validationEngine.validate(
                matchingValidators,
                context,
                StructuralValidation::validateContext,
                true
        );
    }
}
