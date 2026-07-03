package com.codeshare.airline.schedule.ingestion.validation.orchestrator;

import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.context.AbstractIngestionContext;
import com.codeshare.airline.schedule.ingestion.domain.context.AsmIngestionContext;
import com.codeshare.airline.schedule.ingestion.domain.context.SsimIngestionContext;
import com.codeshare.airline.schedule.ingestion.domain.context.SsmIngestionContext;
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

        return switch (context.getMessageType()) {

            case ASM -> validateAsm((AsmIngestionContext) context);

            case SSM -> validateSsm((SsmIngestionContext) context);

            case SSIM -> validateSsim((SsimIngestionContext) context);
        };
    }

    /* ================= ASM ================= */

    private ValidationResult validateAsm(AsmIngestionContext context) {

        return validateByType(MessageType.ASM, context);
    }

    /* ================= SSM ================= */

    private ValidationResult validateSsm(SsmIngestionContext context) {

        return validateByType(MessageType.SSM, context);
    }

    /* ================= SSIM ================= */

    private ValidationResult validateSsim(SsimIngestionContext context) {

        return validateByType(MessageType.SSIM, context);
    }

    @SuppressWarnings("unchecked")
    private <T extends AbstractIngestionContext<?, ?>> ValidationResult validateByType(MessageType type, T context) {
        List<BusinessValidation<?>> matchingValidators = validators.stream()
                .filter(validator -> validator.supportedTypes().contains(type))
                .toList();

        return validationEngine.validate(
                matchingValidators,
                context,
                (validator, target) -> ((BusinessValidation<T>) validator).validate(target),
                false
        );
    }
}
