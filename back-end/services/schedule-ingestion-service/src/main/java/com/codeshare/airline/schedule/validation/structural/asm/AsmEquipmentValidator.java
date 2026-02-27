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
public class AsmEquipmentValidator
        implements Validator<AsmInboundMessage> {

    @Override
    public ValidationResult validate(AsmInboundMessage msg) {

        List<ValidationMessage> errors = new ArrayList<>();

        String eqt = msg.getAircraftType();

        if (eqt != null && !eqt.matches("^[A-Z0-9]{2,4}$")) {

            errors.add(new ValidationMessage(
                    "ASM_014",
                    "Invalid equipment format",
                    ValidationSeverity.WARNING
            ));
        }

        return ValidationResult.of(errors);
    }
}