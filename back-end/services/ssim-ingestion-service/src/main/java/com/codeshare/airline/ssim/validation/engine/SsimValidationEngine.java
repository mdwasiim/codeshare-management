package com.codeshare.airline.ssim.validation.engine;

import com.codeshare.airline.ssim.ingestion.contex.FlightContext;
import com.codeshare.airline.ssim.persistence.inbound.entity.SsimInboundTrailer;
import com.codeshare.airline.ssim.validation.context.ValidationContext;

public interface SsimValidationEngine {

    void validateRecord(
            String line,
            ValidationContext ctx
    );

    void validateFlightTransition(
            FlightContext flightCtx,
            ValidationContext ctx
    );

    void validateTrailer(
            SsimInboundTrailer trailer,
            ValidationContext ctx
    );
}
