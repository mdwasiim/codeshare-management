package com.codeshare.airline.inbound.validations.validator.ssm.business;

import com.codeshare.airline.inbound.domain.context.SsmIngestionContext;
import com.codeshare.airline.inbound.validations.model.ValidationResult;
import com.codeshare.airline.inbound.validations.validator.BusinessValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(2)
public class SsmAirportValidation implements BusinessValidation<SsmIngestionContext> {

    private static final String AIRPORT_REGEX = "^[A-Z]{3}$";

    @Override
    public ValidationResult validate(SsmIngestionContext context) {

        ValidationResult result = new ValidationResult();

        if (context == null || context.getParsedData() == null) {
            return result;
        }

        /*var messages = context.getParsedData().getMessages();

        BaseScheduleValidationHelper.forEachSegment(messages, (msg, flight, leg, seg) -> {

            String origin = seg.getBoardPoint();
            String dest = seg.getOffPoint();

            if (ScheduleValidationUtils.isInvalidAirport(seg)) {
                result.addError("ASM_APT_001",
                        "Invalid airport",
                        flight.getFlightNumber(),
                        ""+seg.getSegmentSequenceNumber(), ValidationStage.BUSINESS);
            }

            if (origin != null && origin.equals(dest)) {
                result.addError("ASM_APT_002",
                        "Same board/off airport",
                        flight.getFlightNumber(),
                        ""+seg.getSegmentSequenceNumber(), ValidationStage.BUSINESS);
            }
        });*/

        return result;
    }
}