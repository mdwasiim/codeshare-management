package com.codeshare.airline.ssim.validation.rule.impl;

import com.codeshare.airline.ssim.validation.context.ValidationContext;
import com.codeshare.airline.ssim.validation.exception.SsimValidationException;
import com.codeshare.airline.ssim.validation.rule.RecordRule;
import com.codeshare.airline.ssim.validation.severity.ValidationSeverity;
import org.springframework.stereotype.Component;

@Component
public class RecordLengthRule implements RecordRule {

    @Override
    public void validate(String line, ValidationContext ctx) {

        if (line == null || line.length() != 200) {
            throw new SsimValidationException(
                    ValidationSeverity.ERROR,
                    "Invalid SSIM record length"
            );
        }
    }
}
