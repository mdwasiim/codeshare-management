package com.codeshare.airline.schedule.validation.structural.ssm;

import com.codeshare.airline.schedule.parsing.ssm.dto.SsmInboundMessage;
import com.codeshare.airline.schedule.validation.core.Validator;
import com.codeshare.airline.schedule.validation.model.ValidationMessage;
import com.codeshare.airline.schedule.validation.model.ValidationResult;
import com.codeshare.airline.schedule.validation.model.ValidationSeverity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SsmEquipmentValidator
        implements Validator<SsmInboundMessage> {

    @Override
    public ValidationResult validate(SsmInboundMessage msg) {

        String eqt = msg.getAircraftType();

        if (eqt != null && !eqt.matches("^[A-Z0-9]{2,4}$")) {

            return ValidationResult.of(List.of(
                    new ValidationMessage(
                            "SSM_011",
                            "Invalid aircraft type format",
                            ValidationSeverity.WARNING
                    )
            ));
        }

        return ValidationResult.valid();
    }
}