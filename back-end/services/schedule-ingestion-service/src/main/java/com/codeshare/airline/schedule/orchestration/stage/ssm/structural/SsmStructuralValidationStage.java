package com.codeshare.airline.schedule.orchestration.stage.ssm.structural;

import com.codeshare.airline.schedule.domain.contex.SsmIngestionContext;
import com.codeshare.airline.schedule.validation.model.ValidationResult;

@Component
public class SsmStructuralValidationStage {

    public void execute(SsmIngestionContext context) {

        ValidationResult result = new ValidationResult();

        String raw = context.getRawContent();

        if (!raw.contains("SSM")) {
            result.addBlocking("Invalid SSM header");
        }

        context.setStructuralResult(result);
    }
}