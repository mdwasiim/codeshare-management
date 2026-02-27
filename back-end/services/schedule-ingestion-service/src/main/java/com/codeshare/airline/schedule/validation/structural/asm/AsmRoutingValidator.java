package com.codeshare.airline.schedule.validation.structural.asm;

import com.codeshare.airline.schedule.parsing.asm.dto.AsmInboundLeg;
import com.codeshare.airline.schedule.parsing.asm.dto.AsmInboundMessage;
import com.codeshare.airline.schedule.validation.core.Validator;
import com.codeshare.airline.schedule.validation.model.ValidationMessage;
import com.codeshare.airline.schedule.validation.model.ValidationResult;
import com.codeshare.airline.schedule.validation.model.ValidationSeverity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AsmRoutingValidator
        implements Validator<AsmInboundMessage> {

    @Override
    public ValidationResult validate(AsmInboundMessage msg) {

        List<ValidationMessage> errors = new ArrayList<>();

        if (msg.getLegs() == null) {
            return ValidationResult.valid();
        }

        for (AsmInboundLeg leg : msg.getLegs()) {

            if (leg.getOrigin() == null ||
                    leg.getDestination() == null) {

                errors.add(new ValidationMessage(
                        "ASM_012",
                        "Invalid routing airports",
                        ValidationSeverity.ERROR
                ));
            }

            if (leg.getDepartureTime() == null ||
                    leg.getArrivalTime() == null) {

                errors.add(new ValidationMessage(
                        "ASM_013",
                        "Missing departure/arrival time",
                        ValidationSeverity.ERROR
                ));
            }
        }

        return ValidationResult.of(errors);
    }
}