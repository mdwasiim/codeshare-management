package com.codeshare.airline.inbound.validations.orchestrator;

import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.inbound.domain.context.AbstractIngestionContext;
import com.codeshare.airline.inbound.domain.context.AsmIngestionContext;
import com.codeshare.airline.inbound.domain.context.SsimIngestionContext;
import com.codeshare.airline.inbound.domain.context.SsmIngestionContext;
import com.codeshare.airline.inbound.validations.engine.ValidationEngine;
import com.codeshare.airline.inbound.validations.model.ValidationResult;
import com.codeshare.airline.inbound.validations.validator.StructuralValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StructuralValidationOrchestrator {

    private final List<StructuralValidation<?>> validators;

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
        List<StructuralValidation<?>> matchingValidators = validators.stream()
                .filter(validator -> validator.supportedTypes().contains(type))
                .toList();

        return validationEngine.validate(
                matchingValidators,
                context,
                (validator, target) -> ((StructuralValidation<T>) validator).validate(target),
                true
        );
    }
}
