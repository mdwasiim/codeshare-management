package com.codeshare.airline.ssim.inbound.validation.business;

import com.codeshare.airline.ssim.inbound.parsing.processor.dto.SsimInboundFileDTO;
import com.codeshare.airline.ssim.inbound.validation.model.ValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BusinessValidationEngine {

    private final List<BusinessValidationRule> rules;

    public ValidationResult validate(SsimInboundFileDTO inboundFile) {

        BusinessValidationContext context =
                new BusinessValidationContext(inboundFile);

        for (BusinessValidationRule rule : rules) {
            rule.validate(context);
        }

        return new ValidationResult(context.getMessages());
    }
}
