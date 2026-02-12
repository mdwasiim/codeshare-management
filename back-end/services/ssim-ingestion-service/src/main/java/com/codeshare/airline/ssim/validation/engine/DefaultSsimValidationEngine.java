package com.codeshare.airline.ssim.validation.engine;

import com.codeshare.airline.ssim.ingestion.contex.FlightContext;
import com.codeshare.airline.ssim.persistence.inbound.entity.SsimInboundTrailer;
import com.codeshare.airline.ssim.validation.context.ValidationContext;
import com.codeshare.airline.ssim.validation.rule.FlightRule;
import com.codeshare.airline.ssim.validation.rule.OrderRule;
import com.codeshare.airline.ssim.validation.rule.RecordRule;
import com.codeshare.airline.ssim.validation.rule.TrailerRule;
import com.codeshare.airline.ssim.validation.rule.impl.AirlineCodeBlankWarnRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DefaultSsimValidationEngine implements SsimValidationEngine {

    private final List<RecordRule> recordRules;
    private final List<OrderRule> orderRules;
    private final List<FlightRule> flightRules;
    private final List<TrailerRule> trailerRules;

    private final AirlineCodeBlankWarnRule airlineCodeBlankWarnRule;

    @Override
    public void validateRecord(String line, ValidationContext ctx) {

        // Record-level validation (length, type, WARN rules, etc.)
        for (RecordRule rule : recordRules) {
            rule.validate(line, ctx);
        }

        // File-level order validation (also increments recordsRead)
        for (OrderRule rule : orderRules) {
            rule.validate(line, ctx);
        }


    }

    @Override
    public void validateFlightTransition(
            FlightContext flightCtx,
            ValidationContext ctx
    ) {
        for (FlightRule rule : flightRules) {
            rule.validate(flightCtx, ctx);
        }
    }


    @Override
    public void validateTrailer(
            SsimInboundTrailer trailer,
            ValidationContext ctx
    ) {
        for (TrailerRule rule : trailerRules) {
            rule.validate(trailer, ctx);
        }
    }
}
