package com.codeshare.airline.inbound.validations.validator.asm.business;

import com.codeshare.airline.inbound.domain.context.AsmIngestionContext;
import com.codeshare.airline.inbound.validations.model.ValidationResult;
import com.codeshare.airline.inbound.validations.validator.BusinessValidation;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
public class AsmAirportValidation implements BusinessValidation<AsmIngestionContext> {

    @Override
    public ValidationResult validate(AsmIngestionContext context) {

        ValidationResult result = new ValidationResult();

        if (context == null || context.getParsedData() == null) {
            return result;
        }

        /*ScheduleMessageDTO message = context.getParsedData();

        BaseScheduleValidationHelper.forEachSegment(message, (msg, flight, leg, seg) -> {

            if (seg == null) return;

            String origin = seg.getBoardPoint();
            String dest = seg.getOffPoint();
            String flightNo = flight != null ? flight.getFlightNumber() : "UNKNOWN";
            String segmentKey = seg.getSegmentSequenceNumber() != null
                    ? String.valueOf(seg.getSegmentSequenceNumber())
                    : "UNKNOWN";

            // Invalid airport format
            if (ScheduleValidationUtils.isInvalidAirport(seg)) {
                result.addError(
                        "ASM_APT_001",
                        "Invalid airport",
                        "SEGMENT",
                        flightNo + "-" + segmentKey,
                        ValidationStage.BUSINESS
                );
            }

            // Same origin/destination
            if (origin != null && origin.equals(dest)) {
                result.addError(
                        "ASM_APT_002",
                        "Same board/off airport",
                        "SEGMENT",
                        flightNo + "-" + segmentKey,
                        ValidationStage.BUSINESS
                );
            }
        });*/

        return result;
    }
}