package com.codeshare.airline.schedule.validation.business.ssm;

import com.codeshare.airline.schedule.parsing.common.dto.ActionIdentifier;
import com.codeshare.airline.schedule.parsing.ssm.dto.SsmInboundMessage;
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
public class SsmDuplicateFlightValidator
        implements Validator<SsmInboundMessage> {

    private final ScheduleFlightRepository flightRepo;

    @Override
    public ValidationResult validate(SsmInboundMessage msg) {

        List<ValidationMessage> messages = new ArrayList<>();

        boolean exists = flightRepo.existsByCarrierAndFlightNumberAndDate(
                msg.getCarrier(),
                msg.getFlightNumber(),
                msg.getOperationDate()
        );

        if (msg.getActionIdentifier() == ActionIdentifier.NEW && exists) {

            messages.add(new ValidationMessage(
                    "SSM_B_004",
                    "Flight already exists for NEW action",
                    ValidationSeverity.ERROR
            ));
        }

        if (msg.getActionIdentifier() == ActionIdentifier.CNL && !exists) {

            messages.add(new ValidationMessage(
                    "SSM_B_005",
                    "Cannot cancel non-existing flight",
                    ValidationSeverity.ERROR
            ));
        }

        return ValidationResult.of(messages);
    }
}