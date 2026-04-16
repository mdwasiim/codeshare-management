package com.codeshare.airline.ingestion.validations.orchestrator;

import com.codeshare.airline.ingestion.domain.context.AbstractIngestionContext;
import com.codeshare.airline.ingestion.domain.context.AsmIngestionContext;
import com.codeshare.airline.ingestion.domain.context.SsimIngestionContext;
import com.codeshare.airline.ingestion.domain.context.SsmIngestionContext;
import com.codeshare.airline.ingestion.validations.engine.ValidationEngine;
import com.codeshare.airline.ingestion.validations.model.ValidationResult;
import com.codeshare.airline.ingestion.validations.validator.StructuralValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StructuralValidationOrchestrator {

    private final List<StructuralValidation<AsmIngestionContext>> asmValidators;
    private final List<StructuralValidation<SsmIngestionContext>> ssmValidators;
    private final List<StructuralValidation<SsimIngestionContext>> ssimValidators;

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

        return validationEngine.validate(
                asmValidators,
                context,
                StructuralValidation::validate,
                true
        );
    }

    /* ================= SSM ================= */

    private ValidationResult validateSsm(SsmIngestionContext context) {

        return validationEngine.validate(
                ssmValidators,
                context,
                StructuralValidation::validate,
                true
        );
    }

    /* ================= SSIM ================= */

    private ValidationResult validateSsim(SsimIngestionContext context) {

        return validationEngine.validate(
                ssimValidators,
                context,
                StructuralValidation::validate,
                true
        );
    }
}