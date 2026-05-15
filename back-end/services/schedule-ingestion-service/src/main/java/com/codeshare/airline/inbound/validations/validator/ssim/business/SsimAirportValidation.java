package com.codeshare.airline.inbound.validations.validator.ssim.business;

import com.codeshare.airline.core.enums.MessageType;
import com.codeshare.airline.inbound.domain.context.SsimIngestionContext;
import com.codeshare.airline.inbound.validations.model.ValidationResult;
import com.codeshare.airline.inbound.validations.validator.BusinessValidation;
import lombok.AllArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Order(4)
@AllArgsConstructor
public class SsimAirportValidation implements BusinessValidation<SsimIngestionContext> {

    @Override
    public Set<MessageType> supportedTypes() {
        return Set.of(MessageType.SSIM);
    }

    @Override
    public ValidationResult validate(SsimIngestionContext context) {

        ValidationResult result = new ValidationResult();

        /*if (context == null || context.getMessages() == null) {
            return result;
        }

        for (SSIMMessageDTO msg : context.getMessages()) {

            if (msg == null) continue;

            String flightNo = msg.getFlightNumber() != null
                    ? msg.getFlightNumber()
                    : "UNKNOWN";

            List<SsimFlightDTO> legs =
                    msg.getLegs() != null ? msg.getLegs() : List.of();

            for (SsimFlightDTO leg : legs) {

                if (leg == null) continue;

                String legKey = leg.getLegSequenceNumber() != null
                        ? String.valueOf(leg.getLegSequenceNumber())
                        : "UNKNOWN";

                String recordKey = flightNo + "-" + legKey;

                // Departure validation
                if (ScheduleValidationUtils.isInvalidAirport(leg.getDepartureStation())) {
                    result.addError(
                            "BUS_011",
                            "Invalid departure airport",
                            "FLIGHT_LEG",
                            recordKey,
                            ValidationStage.BUSINESS
                    );
                }

                // Arrival validation
                if (ScheduleValidationUtils.isInvalidAirport(leg.getArrivalStation())) {
                    result.addError(
                            "BUS_012",
                            "Invalid arrival airport",
                            "FLIGHT_LEG",
                            recordKey,
                            ValidationStage.BUSINESS
                    );
                }
            }
        }*/

        return result;
    }
}
