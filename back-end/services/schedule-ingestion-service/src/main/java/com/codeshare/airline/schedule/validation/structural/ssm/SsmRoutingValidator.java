package com.codeshare.airline.schedule.validation.structural.ssm;

import com.codeshare.airline.schedule.parsing.ssm.dto.SsmInboundLeg;
import com.codeshare.airline.schedule.parsing.ssm.dto.SsmInboundMessage;
import com.codeshare.airline.schedule.validation.core.Validator;
import com.codeshare.airline.schedule.validation.model.ValidationMessage;
import com.codeshare.airline.schedule.validation.model.ValidationResult;
import com.codeshare.airline.schedule.validation.model.ValidationSeverity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SsmRoutingValidator
        implements Validator<SsmInboundMessage> {

    @Override
    public ValidationResult validate(SsmInboundMessage msg) {

        List<ValidationMessage> errors = new ArrayList<>();

        if (msg.getLegs() == null) {
            return ValidationResult.valid();
        }

        for (SsmInboundLeg leg : msg.getLegs()) {

            if (leg.getOrigin() == null ||
                    leg.getDestination() == null) {

                errors.add(new ValidationMessage(
                        "SSM_007",
                        "Invalid routing airports",
                        ValidationSeverity.ERROR
                ));
            }

            if (leg.getDepartureTime() == null ||
                    leg.getArrivalTime() == null) {

                errors.add(new ValidationMessage(
                        "SSM_008",
                        "Missing departure/arrival time",
                        ValidationSeverity.ERROR
                ));
            }
        }

        return ValidationResult.of(errors);
    }
}