package com.codeshare.airline.inbound.validations.validator.asm.business;

import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.inbound.domain.context.AsmIngestionContext;
import com.codeshare.airline.inbound.domain.enums.ValidationStage;
import com.codeshare.airline.inbound.validations.model.ValidationResult;
import com.codeshare.airline.inbound.validations.validator.BusinessValidation;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Order(3)
public class AsmAirportValidation implements BusinessValidation<AsmIngestionContext> {

    @Override
    public Set<MessageType> supportedTypes() {
        return Set.of(MessageType.ASM);
    }

    @Override
    public ValidationResult validate(AsmIngestionContext context) {

        ValidationResult result = new ValidationResult();

        if (context == null || context.getParsedData() == null) {
            return result;
        }

        context.getParsedData().getMessages().forEach(message ->
                message.getFlights().forEach(flight ->
                        flight.getLegs().forEach(leg -> {
                            validateAirport(result, "ASM_APT_001", leg.getBoardPoint(), flight.getFlightNumber(), "BOARD");
                            validateAirport(result, "ASM_APT_002", leg.getOffPoint(), flight.getFlightNumber(), "OFF");
                            if (isAirport(leg.getBoardPoint())
                                    && leg.getBoardPoint().equalsIgnoreCase(leg.getOffPoint())) {
                                result.addError(
                                        "ASM_APT_003",
                                        "Same board/off airport",
                                        "FLIGHT",
                                        flight.getFlightNumber() + ":" + leg.getBoardPoint() + "-" + leg.getOffPoint(),
                                        ValidationStage.BUSINESS
                                );
                            }
                        })
                )
        );

        return result;
    }

    private void validateAirport(ValidationResult result, String code, String value, String flightNumber, String role) {
        if (!isAirport(value)) {
            result.addError(
                    code,
                    "Invalid " + role.toLowerCase() + " airport",
                    "FLIGHT",
                    flightNumber + ":" + role + ":" + value,
                    ValidationStage.BUSINESS
            );
        }
    }

    private boolean isAirport(String value) {
        return value != null && value.matches("^[A-Z]{3}$");
    }
}
