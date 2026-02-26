package com.codeshare.airline.schedule.validation.ssim.structural;

public class DefaultSsmStructuralValidationStage
        implements SsmStructuralValidationStage {

    @Override
    public void validate(String content) {

        if (!content.contains("2")) {
            throw new IllegalArgumentException("Missing flight record in SSM");
        }
    }
}