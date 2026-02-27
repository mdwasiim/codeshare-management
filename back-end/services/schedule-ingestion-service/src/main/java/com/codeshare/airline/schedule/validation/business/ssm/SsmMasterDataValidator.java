package com.codeshare.airline.schedule.validation.business.ssm;

import com.codeshare.airline.schedule.parsing.ssm.dto.SsmInboundLeg;
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
public class SsmMasterDataValidator
        implements Validator<SsmInboundMessage> {

    private final CarrierRepository carrierRepo;
    private final AirportRepository airportRepo;

    @Override
    public ValidationResult validate(SsmInboundMessage msg) {

        List<ValidationMessage> messages = new ArrayList<>();

        if (!carrierRepo.existsByCode(msg.getCarrier())) {
            messages.add(error("SSM_B_001",
                    "Unknown carrier: " + msg.getCarrier()));
        }

        if (msg.getLegs() != null) {
            for (SsmInboundLeg leg : msg.getLegs()) {

                if (!airportRepo.existsByCode(leg.getOrigin())) {
                    messages.add(error("SSM_B_002",
                            "Unknown origin airport: " + leg.getOrigin()));
                }

                if (!airportRepo.existsByCode(leg.getDestination())) {
                    messages.add(error("SSM_B_003",
                            "Unknown destination airport: " + leg.getDestination()));
                }
            }
        }

        return ValidationResult.of(messages);
    }

    private ValidationMessage error(String code, String msg) {
        return new ValidationMessage(code, msg, ValidationSeverity.ERROR);
    }
}