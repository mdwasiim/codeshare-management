package com.codeshare.airline.schedule.validation.structural.ssim.rule;

import com.codeshare.airline.schedule.validation.model.ValidationMessage;
import com.codeshare.airline.schedule.orchestration.stage.ssim.structural.context.SsimStructuralValidationContext;
import org.springframework.stereotype.Component;

@Component
public class RecordTypeRule implements StructuralValidationRule {

    @Override
    public void validate(String line, SsimStructuralValidationContext ctx) {

        if (line == null || line.isEmpty()) {
            ctx.addMessage(
                    ValidationMessage.blocking(
                            "EMPTY_LINE",
                            "Empty record at line " + ctx.getLineNumber()
                    )
            );
            return;
        }
        line = line.replaceAll("^[\\r\\n]+", "");
        char recordType = line.charAt(0);

        if (recordType < '0' || recordType > '5') {

            ctx.addMessage(
                    ValidationMessage.blocking(
                            "UNKNOWN_RECORD_TYPE",
                            "Unknown SSIM record type '" + recordType +
                                    "' at line " + ctx.getLineNumber()
                    )
            );
        }
    }
}
