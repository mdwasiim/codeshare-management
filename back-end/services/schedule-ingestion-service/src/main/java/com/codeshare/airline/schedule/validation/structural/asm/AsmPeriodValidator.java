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
public class AsmPeriodValidator
        implements Validator<AsmInboundMessage> {

    @Override
    public ValidationResult validate(AsmInboundMessage msg) {

        List<ValidationMessage> errors = new ArrayList<>();

        if (msg.getPeriodFrom() != null &&
                msg.getPeriodTo() != null &&
                msg.getPeriodTo().isBefore(msg.getPeriodFrom())) {

            errors.add(new ValidationMessage(
                    "ASM_010",
                    "PeriodTo cannot be before PeriodFrom",
                    ValidationSeverity.ERROR
            ));
        }

        return ValidationResult.of(errors);
    }
}