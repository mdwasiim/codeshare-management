package com.codeshare.airline.schedule.validation.structural.ssim.rule;

import com.codeshare.airline.schedule.validation.model.ValidationMessage;
import com.codeshare.airline.schedule.orchestration.stage.ssim.structural.context.SsimStructuralValidationContext;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class RecordLengthRule implements StructuralValidationRule {

    private static final int EXPECTED_LENGTH = 200;

    @Override
    public void validate(String line, SsimStructuralValidationContext context) {

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
