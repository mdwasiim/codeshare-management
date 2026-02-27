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
public class SsmActionSpecificValidator
        implements Validator<SsmInboundMessage> {

    @Override
    public ValidationResult validate(SsmInboundMessage msg) {

        List<ValidationMessage> errors = new ArrayList<>();

        ActionIdentifier action = msg.getActionIdentifier();

        if (action == null) {
            return ValidationResult.valid();
        }

        switch (action) {

            case RIN -> {
                if (msg.getLegs() == null || msg.getLegs().size() < 1) {
                    errors.add(new ValidationMessage(
                            "SSM_009",
                            "RIN requires at least one routing leg",
                            ValidationSeverity.ERROR
                    ));
                }
            }

            case RRT -> {
                if (msg.getLegs() == null || msg.getLegs().isEmpty()) {
                    errors.add(new ValidationMessage(
                            "SSM_010",
                            "RRT requires routing definition",
                            ValidationSeverity.ERROR
                    ));
                }
            }
        }

        return ValidationResult.of(errors);
    }
}