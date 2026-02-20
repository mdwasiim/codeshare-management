package com.codeshare.airline.ssim.inbound.validation.structural.rule;

import com.codeshare.airline.ssim.inbound.domain.contex.StructuralValidationContext;

public interface StructuralValidationRule {

    void validate(String line, StructuralValidationContext context);

    default void afterFileComplete(StructuralValidationContext context) {
        // Default empty implementation
    }
}