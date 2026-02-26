package com.codeshare.airline.schedule.validation.ssim.structural.rule;

import com.codeshare.airline.schedule.orchestration.stage.ssim.structural.model.SsimStructuralValidationContext;

public interface StructuralValidationRule {

    void validate(String line, SsimStructuralValidationContext context);

    default void afterFileComplete(SsimStructuralValidationContext context) {
        // Default empty implementation
    }
}