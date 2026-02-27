package com.codeshare.airline.schedule.validation.structural.ssim.rule;

import com.codeshare.airline.schedule.orchestration.stage.ssim.structural.context.SsimStructuralValidationContext;

public interface StructuralValidationRule {

    void validate(String line, SsimStructuralValidationContext context);

    default void afterFileComplete(SsimStructuralValidationContext context) {
        // Default empty implementation
    }
}