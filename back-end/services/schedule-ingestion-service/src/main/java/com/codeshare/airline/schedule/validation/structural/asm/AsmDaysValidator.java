package com.codeshare.airline.schedule.validation.structural.asm;

import com.codeshare.airline.schedule.parsing.asm.dto.AsmInboundMessage;
import com.codeshare.airline.schedule.validation.core.Validator;
import com.codeshare.airline.schedule.validation.model.ValidationMessage;
import com.codeshare.airline.schedule.validation.model.ValidationResult;
import com.codeshare.airline.schedule.validation.model.ValidationSeverity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AsmDaysValidator
        implements Validator<AsmInboundMessage> {

    @Override
    public ValidationResult validate(AsmInboundMessage msg) {

        List<ValidationMessage> errors = new ArrayList<>();
        String days = msg.getDaysOfOperation();

        if (days == null) {
            return ValidationResult.valid();
        }

        if (!days.matches("^[1-7]{1,7}$")) {
            errors.add(new ValidationMessage(
                    "ASM_011",
                    "Invalid days-of-operation format",
                    ValidationSeverity.ERROR
            ));
        }

        return ValidationResult.of(errors);
    }
}