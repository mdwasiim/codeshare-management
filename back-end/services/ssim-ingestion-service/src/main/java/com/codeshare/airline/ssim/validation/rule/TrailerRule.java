package com.codeshare.airline.ssim.validation.rule;

import com.codeshare.airline.ssim.persistence.inbound.entity.SsimInboundTrailer;
import com.codeshare.airline.ssim.validation.context.ValidationContext;

public interface TrailerRule {
    void validate(
            SsimInboundTrailer trailer,
            ValidationContext ctx
    );
}
