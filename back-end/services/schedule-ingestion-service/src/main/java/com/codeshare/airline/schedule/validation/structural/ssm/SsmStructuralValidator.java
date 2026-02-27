package com.codeshare.airline.schedule.validation.structural.ssm;

import com.codeshare.airline.schedule.parsing.ssm.dto.SsmInboundMessage;
import com.codeshare.airline.schedule.validation.core.Validator;
import com.codeshare.airline.schedule.validation.model.ValidationMessage;
import com.codeshare.airline.schedule.validation.model.ValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SsmStructuralValidator
        implements Validator<List<SsmInboundMessage>> {

    private final SsmRequiredFieldsValidator required;
    private final SsmDateValidator date;
    private final SsmRoutingValidator routing;
    private final SsmActionSpecificValidator action;
    private final SsmEquipmentValidator equipment;

    @Override
    public ValidationResult validate(List<SsmInboundMessage> messages) {

        List<ValidationMessage> all = new ArrayList<>();

        for (SsmInboundMessage msg : messages) {

            all.addAll(required.validate(msg).getMessages());
            all.addAll(date.validate(msg).getMessages());
            all.addAll(routing.validate(msg).getMessages());
            all.addAll(action.validate(msg).getMessages());
            all.addAll(equipment.validate(msg).getMessages());
        }

        return ValidationResult.of(all);
    }
}