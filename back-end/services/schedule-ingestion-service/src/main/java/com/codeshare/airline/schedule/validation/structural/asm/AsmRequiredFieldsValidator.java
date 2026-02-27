package com.codeshare.airline.schedule.validation.structural.asm;

import com.codeshare.airline.schedule.parsing.asm.dto.AsmInboundMessage;
import com.codeshare.airline.schedule.parsing.common.dto.ActionIdentifier;
import com.codeshare.airline.schedule.validation.core.Validator;
import com.codeshare.airline.schedule.validation.model.ValidationMessage;
import com.codeshare.airline.schedule.validation.model.ValidationResult;
import com.codeshare.airline.schedule.validation.model.ValidationSeverity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AsmRequiredFieldsValidator
        implements Validator<AsmInboundMessage> {

    @Override
    public ValidationResult validate(AsmInboundMessage msg) {

        List<ValidationMessage> errors = new ArrayList<>();

        ActionIdentifier action = msg.getActionIdentifier();

        if (action == null) {
            errors.add(error("ASM_001", "Missing ActionIdentifier"));
            return ValidationResult.of(errors);
        }

        switch (action) {

            case NEW, RPL, TIM -> {
                require(msg.getPeriodFrom(), "ASM_002", "PeriodFrom required", errors);
                require(msg.getPeriodTo(), "ASM_003", "PeriodTo required", errors);
                require(msg.getDaysOfOperation(), "ASM_004", "DaysOfOperation required", errors);
                requireRouting(msg, errors);
            }

            case CNL -> {
                require(msg.getPeriodFrom(), "ASM_005", "PeriodFrom required for CNL", errors);
                require(msg.getPeriodTo(), "ASM_006", "PeriodTo required for CNL", errors);
            }

            case EQT, ADM -> {
                // minimal required
                require(msg.getCarrier(), "ASM_007", "Carrier required", errors);
                require(msg.getFlightNumber(), "ASM_008", "FlightNumber required", errors);
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

    private void requireRouting(AsmInboundMessage msg,
                                List<ValidationMessage> errors) {

        if (msg.getLegs() == null || msg.getLegs().isEmpty()) {
            errors.add(error("ASM_009", "Routing required"));
        }
    }

    private ValidationMessage error(String code, String msg) {
        return new ValidationMessage(code, msg, ValidationSeverity.ERROR);
    }
}