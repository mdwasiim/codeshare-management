package com.codeshare.airline.ssim.validation.rule.impl;

import com.codeshare.airline.ssim.validation.context.ValidationContext;
import com.codeshare.airline.ssim.validation.exception.SsimValidationException;
import com.codeshare.airline.ssim.validation.rule.RecordRule;
import com.codeshare.airline.ssim.validation.severity.ValidationSeverity;
import org.springframework.stereotype.Component;

@Component
public class RecordTypeRule implements RecordRule {

    @Override
    public void validate(String line, ValidationContext ctx) {

        char recordType = line.charAt(0);

        if (recordType < '1' || recordType > '5') {
            throw new SsimValidationException(
                    ValidationSeverity.ERROR,
                    "Unknown SSIM record type: '" + recordType + "'"
            );
        }
    }
}
