package com.codeshare.airline.ingestion.validations.orchestrator;

import com.codeshare.airline.ingestion.domain.context.AbstractIngestionContext;
import com.codeshare.airline.ingestion.domain.context.AsmIngestionContext;
import com.codeshare.airline.ingestion.domain.context.SsimIngestionContext;
import com.codeshare.airline.ingestion.domain.context.SsmIngestionContext;
import com.codeshare.airline.ingestion.validations.engine.ValidationEngine;
import com.codeshare.airline.ingestion.validations.model.ValidationResult;
import com.codeshare.airline.ingestion.validations.validator.BusinessValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BusinessValidationOrchestrator {

    private final List<BusinessValidation<AsmIngestionContext>> asmValidators;
    private final List<BusinessValidation<SsmIngestionContext>> ssmValidators;
    private final List<BusinessValidation<SsimIngestionContext>> ssimValidators;

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
                BusinessValidation::validate,
                false   // collect all errors
        );
    }

    /* ================= SSM ================= */

    private ValidationResult validateSsm(SsmIngestionContext context) {

        return validationEngine.validate(
                ssmValidators,
                context,
                BusinessValidation::validate,
                false
        );
    }

    /* ================= SSIM ================= */

    private ValidationResult validateSsim(SsimIngestionContext context) {

        return validationEngine.validate(
                ssimValidators,
                context,
                BusinessValidation::validate,
                false
        );
    }
}