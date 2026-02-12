package com.codeshare.airline.ssim.validation.rule;

import com.codeshare.airline.ssim.ingestion.contex.FlightContext;
import com.codeshare.airline.ssim.validation.context.ValidationContext;

public interface FlightRule {
    void validate(FlightContext flightCtx, ValidationContext ctx);
}
