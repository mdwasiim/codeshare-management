package com.codeshare.airline.ssim.validation.rule.impl;


import com.codeshare.airline.ssim.ingestion.contex.FlightContext;
import com.codeshare.airline.ssim.validation.context.ValidationContext;
import com.codeshare.airline.ssim.validation.exception.SsimValidationException;
import com.codeshare.airline.ssim.validation.rule.FlightRule;
import com.codeshare.airline.ssim.validation.severity.ValidationSeverity;
import org.springframework.stereotype.Component;

@Component
public class T4FlightPresenceRule implements FlightRule {

    @Override
    public void validate(
            FlightContext flightCtx,
            ValidationContext ctx
    ) {

        if (flightCtx == null || flightCtx.getFlight() == null) {
            throw new SsimValidationException(ValidationSeverity.ERROR,"T4 record encountered without active T3 flight");

        }
    }
}
