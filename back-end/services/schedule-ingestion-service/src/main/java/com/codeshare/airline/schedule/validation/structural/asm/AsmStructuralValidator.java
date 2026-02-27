package com.codeshare.airline.schedule.validation.structural.asm;

import com.codeshare.airline.schedule.parsing.asm.dto.AsmInboundMessage;
import com.codeshare.airline.schedule.validation.core.Validator;
import com.codeshare.airline.schedule.validation.model.ValidationMessage;
import com.codeshare.airline.schedule.validation.model.ValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AsmStructuralValidator
        implements Validator<List<AsmInboundMessage>> {

    private final AsmRequiredFieldsValidator required;
    private final AsmPeriodValidator period;
    private final AsmDaysValidator days;
    private final AsmRoutingValidator routing;
    private final AsmEquipmentValidator equipment;

    @Override
    public ValidationResult validate(List<AsmInboundMessage> messages) {

        List<ValidationMessage> all = new ArrayList<>();

        for (AsmInboundMessage msg : messages) {

            all.addAll(required.validate(msg).getMessages());
            all.addAll(period.validate(msg).getMessages());
            all.addAll(days.validate(msg).getMessages());
            all.addAll(routing.validate(msg).getMessages());
            all.addAll(equipment.validate(msg).getMessages());
        }

        return ValidationResult.of(all);
    }
}