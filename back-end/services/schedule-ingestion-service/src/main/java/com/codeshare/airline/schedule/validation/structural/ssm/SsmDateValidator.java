package com.codeshare.airline.schedule.validation.structural.ssm;

import com.codeshare.airline.schedule.parsing.ssm.dto.SsmInboundMessage;
import com.codeshare.airline.schedule.validation.core.Validator;
import com.codeshare.airline.schedule.validation.model.ValidationMessage;
import com.codeshare.airline.schedule.validation.model.ValidationResult;
import com.codeshare.airline.schedule.validation.model.ValidationSeverity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class SsmDateValidator
        implements Validator<SsmInboundMessage> {

    @Override
    public ValidationResult validate(SsmInboundMessage msg) {

        if (msg.getOperationDate() == null) {
            return ValidationResult.valid();
        }

        if (msg.getOperationDate().isBefore(LocalDate.now().minusYears(2))) {
            return ValidationResult.of(List.of(
                    new ValidationMessage(
                            "SSM_006",
                            "OperationDate too far in past",
                            ValidationSeverity.WARNING
                    )
            ));
        }

        return ValidationResult.valid();
    }
}