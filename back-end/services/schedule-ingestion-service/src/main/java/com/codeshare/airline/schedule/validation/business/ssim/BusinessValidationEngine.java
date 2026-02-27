package com.codeshare.airline.schedule.validation.business.ssim;

import com.codeshare.airline.schedule.parsing.ssim.dto.SsimParsedFile;
import com.codeshare.airline.schedule.validation.model.ValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BusinessValidationEngine {

    private final List<BusinessValidationRule> rules;

    public ValidationResult validate(SsimParsedFile inboundFile) {

        BusinessValidationContext context =
                new BusinessValidationContext(inboundFile);

        for (BusinessValidationRule rule : rules) {
            rule.validate(context);
        }

        return new ValidationResult(context.getMessages());
    }
}
