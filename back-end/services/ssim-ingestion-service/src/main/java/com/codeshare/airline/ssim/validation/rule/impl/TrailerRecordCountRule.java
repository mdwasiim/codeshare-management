package com.codeshare.airline.ssim.validation.rule.impl;


import com.codeshare.airline.ssim.persistence.inbound.entity.SsimInboundTrailer;
import com.codeshare.airline.ssim.validation.context.ValidationContext;
import com.codeshare.airline.ssim.validation.exception.SsimValidationException;
import com.codeshare.airline.ssim.validation.rule.TrailerRule;
import com.codeshare.airline.ssim.validation.severity.ValidationSeverity;
import org.springframework.stereotype.Component;

@Component
public class TrailerRecordCountRule implements TrailerRule {

    @Override
    public void validate(
            SsimInboundTrailer trailer,
            ValidationContext ctx
    ) {

        long declaredCount;

        try {
            declaredCount =
                    Long.parseLong(trailer.getTotalRecordCountRaw().trim());
        } catch (NumberFormatException ex) {

            throw new SsimValidationException(ValidationSeverity.ERROR,"Invalid T5 total record count: '" + trailer.getTotalRecordCountRaw() + "'");

        }

        if (declaredCount != ctx.getRecordsRead()) {

            throw new SsimValidationException(ValidationSeverity.ERROR,"T5 record count mismatch. Declared=" + declaredCount + ", Actual=" + ctx.getRecordsRead());

        }
    }
}
