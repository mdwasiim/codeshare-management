package com.codeshare.airline.schedule.validation.structural.ssm;

import com.codeshare.airline.schedule.parsing.common.dto.ActionIdentifier;
import com.codeshare.airline.schedule.parsing.ssm.dto.SsmInboundMessage;
import com.codeshare.airline.schedule.validation.core.Validator;
import com.codeshare.airline.schedule.validation.model.ValidationMessage;
import com.codeshare.airline.schedule.validation.model.ValidationResult;
import com.codeshare.airline.schedule.validation.model.ValidationSeverity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SsmRequiredFieldsValidator
        implements Validator<SsmInboundMessage> {

    @Override
    public ValidationResult validate(SsmInboundMessage msg) {

        List<ValidationMessage> errors = new ArrayList<>();

        ActionIdentifier action = msg.getActionIdentifier();

        if (action == null) {
            errors.add(error("SSM_001", "Missing ActionIdentifier"));
            return ValidationResult.of(errors);
        }

        require(msg.getCarrier(), "SSM_002", "Carrier required", errors);
        require(msg.getFlightNumber(), "SSM_003", "FlightNumber required", errors);
        require(msg.getOperationDate(), "SSM_004", "OperationDate required", errors);

        switch (action) {

            case NEW, TIM, RRT -> requireRouting(msg, errors);

            case CNL -> {
                // CNL may not require routing
            }

            case RIN -> {
                // RIN requires routing reference consistency
                requireRouting(msg, errors);
            }

            case EQT, ADM -> {
                // Minimal structural requirement
            }
        }

        return ValidationResult.of(errors);
    }

    private void require(Object value,
                         String code,
                         String message,
                         List<ValidationMessage> errors) {

        if (value == null ||
                (value instanceof String s && s.isBlank())) {

            errors.add(error(code, message));
        }
    }

    private void requireRouting(SsmInboundMessage msg,
                                List<ValidationMessage> errors) {

        if (msg.getLegs() == null || msg.getLegs().isEmpty()) {
            errors.add(error("SSM_005", "Routing required"));
        }
    }

    private ValidationMessage error(String code, String msg) {
        return new ValidationMessage(code, msg, ValidationSeverity.ERROR);
    }
}