package com.codeshare.airline.ssim.validation.rule.impl;


import com.codeshare.airline.ssim.validation.context.ValidationContext;
import com.codeshare.airline.ssim.validation.exception.SsimValidationException;
import com.codeshare.airline.ssim.validation.rule.OrderRule;
import com.codeshare.airline.ssim.validation.severity.ValidationSeverity;
import org.springframework.stereotype.Component;

@Component
public class OrderRuleImpl implements OrderRule {

    @Override
    public void validate(String line, ValidationContext ctx) {

        char type = line.charAt(0);

        // count records as they stream
        ctx.setRecordsRead(ctx.getRecordsRead() + 1);

        switch (type) {

            case '1' -> {
                if (ctx.isT1Seen()) {
                    throw new SsimValidationException(ValidationSeverity.ERROR,"Duplicate T1 record: '" + type + "'");
                }
                ctx.setT1Seen(true);
            }

            case '2' -> {
                if (!ctx.isT1Seen()) {
                    throw new SsimValidationException(ValidationSeverity.ERROR,"T2 record before T1: '" + type + "'");
                }
                if (ctx.isT2Seen()) {
                    throw new SsimValidationException(ValidationSeverity.ERROR,"Duplicate T2 record: '" + type + "'");
                }
                ctx.setT2Seen(true);
            }

            case '3' -> {
                if (!ctx.isT2Seen()) {
                    throw new SsimValidationException(ValidationSeverity.ERROR,"T3 record before T2: '" + type + "'");
                }
                ctx.setT3Seen(true);
            }

            case '4' -> {
                if (!ctx.isT3Seen()) {
                    throw new SsimValidationException(ValidationSeverity.ERROR,"T4 record before any T3: '" + type + "'");
                }
            }

            case '5' -> {
                if (ctx.isT5Seen()) {
                    throw new SsimValidationException(ValidationSeverity.ERROR,"Duplicate T5 record: '" + type + "'");
                }
                ctx.setT5Seen(true);
            }

            default -> {
                // should never happen (RecordTypeRule already guards this)
            }
        }
    }
}
