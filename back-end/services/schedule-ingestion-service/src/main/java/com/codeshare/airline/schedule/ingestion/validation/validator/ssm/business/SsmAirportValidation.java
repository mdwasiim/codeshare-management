package com.codeshare.airline.schedule.ingestion.validation.validator.ssm.business;

import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.context.SsmIngestionContext;
import com.codeshare.airline.schedule.ingestion.domain.enums.ValidationStage;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationResult;
import com.codeshare.airline.schedule.ingestion.validation.validator.BusinessValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@Order(2)
public class SsmAirportValidation implements BusinessValidation<SsmIngestionContext> {

    @Override
    public Set<MessageType> supportedTypes() {
        return Set.of(MessageType.SSM);
    }

    @Override
    public ValidationResult validate(SsmIngestionContext context) {

        ValidationResult result = new ValidationResult();

        if (context == null || context.getParsedData() == null) {
            return result;
        }

        context.getParsedData().getMessages().forEach(message ->
                message.getFlights().forEach(flight ->
                        flight.getLegs().forEach(leg -> {
                            validateAirport(result, "SSM_APT_001", leg.getBoardPoint(), flight.getFlightNumber(), "BOARD");
                            validateAirport(result, "SSM_APT_002", leg.getOffPoint(), flight.getFlightNumber(), "OFF");
                            if (isAirport(leg.getBoardPoint())
                                    && leg.getBoardPoint().equalsIgnoreCase(leg.getOffPoint())) {
                                result.addError(
                                        "SSM_APT_003",
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
