package com.codeshare.airline.schedule.validation.business.ssm;

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
public class SsmBusinessValidator
        implements Validator<List<SsmInboundMessage>> {

    private final SsmMasterDataValidator master;
    private final SsmDuplicateFlightValidator duplicate;

    @Override
    public ValidationResult validate(List<SsmInboundMessage> messages) {

        List<ValidationMessage> all = new ArrayList<>();

        for (SsmInboundMessage msg : messages) {

            all.addAll(master.validate(msg).getMessages());
            all.addAll(duplicate.validate(msg).getMessages());
        }

        return ValidationResult.of(all);
    }
}