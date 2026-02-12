package com.codeshare.airline.ssim.validation.rule.impl;

import com.codeshare.airline.ssim.validation.context.ValidationContext;
import com.codeshare.airline.ssim.validation.rule.RecordRule;
import org.springframework.stereotype.Component;

@Component
public class AirlineCodeBlankWarnRule implements RecordRule {

    @Override
    public void validate(String line, ValidationContext ctx) {

        if (line.charAt(0) == '3') {
            String airline = line.substring(2, 5).trim();
            if (airline.isEmpty()) {
                ctx.warn(
                        "Blank airline code in T3 record"
                );
            }
        }
    }
}
