package com.codeshare.airline.schedule.validation.business.asm;

import com.codeshare.airline.schedule.parsing.asm.dto.AsmInboundMessage;
import com.codeshare.airline.schedule.parsing.common.dto.ActionIdentifier;
import com.codeshare.airline.schedule.validation.core.Validator;
import com.codeshare.airline.schedule.validation.model.ValidationMessage;
import com.codeshare.airline.schedule.validation.model.ValidationResult;
import com.codeshare.airline.schedule.validation.model.ValidationSeverity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AsmPeriodOverlapValidator
        implements Validator<AsmInboundMessage> {

    private final SchedulePeriodRepository repo;

    @Override
    public ValidationResult validate(AsmInboundMessage msg) {

        List<ValidationMessage> errors = new ArrayList<>();

        if (msg.getPeriodFrom() == null || msg.getPeriodTo() == null) {
            return ValidationResult.valid();
        }

        boolean overlap = repo.existsOverlappingPeriod(
                msg.getCarrier(),
                msg.getFlightNumber(),
                msg.getPeriodFrom(),
                msg.getPeriodTo()
        );

        if (overlap &&
                msg.getActionIdentifier() == ActionIdentifier.NEW) {

            errors.add(new ValidationMessage(
                    "ASM_B_001",
                    "Period overlaps existing schedule",
                    ValidationSeverity.ERROR
            ));
        }

        return ValidationResult.of(errors);
    }
}