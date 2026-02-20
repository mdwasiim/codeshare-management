package com.codeshare.airline.ssim.inbound.validation.structural.rule;

import com.codeshare.airline.ssim.inbound.validation.model.ValidationMessage;
import com.codeshare.airline.ssim.inbound.domain.contex.StructuralValidationContext;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class RecordLengthRule implements StructuralValidationRule {

    private static final int EXPECTED_LENGTH = 200;

    @Override
    public void validate(String line, StructuralValidationContext context) {

        if (line == null ||
                line.getBytes(StandardCharsets.US_ASCII).length != EXPECTED_LENGTH) {

            context.addMessage(
                    ValidationMessage.blocking(
                            "INVALID_LENGTH",
                            "Invalid record length at line "
                                    + context.getLineNumber()
                    )
            );
        }
    }
}
