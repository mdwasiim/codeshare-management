package com.codeshare.airline.ssim.inbound.validation.structural.rule;


import com.codeshare.airline.ssim.inbound.validation.model.ValidationMessage;
import com.codeshare.airline.ssim.inbound.domain.contex.StructuralValidationContext;
import org.springframework.stereotype.Component;

@Component
public class RecordOrderRule implements StructuralValidationRule {

    @Override
    public void validate(String line, StructuralValidationContext ctx) {

        if (line == null || line.isBlank()) {
            return;
        }

        line = line.replaceAll("^[\\r\\n]+", "");
        char type = line.charAt(0);

        int lineNumber = ctx.getLineNumber();

        // ❗ Nothing allowed after T5
        if (ctx.isT5Seen()) {

            if (isPaddingLine(line)) {
                return; // allowed
            }

            ctx.addMessage(
                    ValidationMessage.blocking(
                            "RECORD_AFTER_T5",
                            "Invalid record found after T5 at line " + ctx.getLineNumber()
                    )
            );

            return;
        }

        switch (type) {

            case '1' -> handleT1(ctx, lineNumber);

            case '2' -> handleT2(ctx, lineNumber);

            case '3' -> handleT3(ctx, lineNumber);

            case '4' -> handleT4(ctx, lineNumber);

            case '5' -> handleT5(ctx, lineNumber);

            default -> {
                // ignore here (other rule handles invalid type)
            }
        }

        ctx.setPreviousRecordType(type);
    }

    private void handleT1(StructuralValidationContext ctx, int lineNumber) {

        if (lineNumber != 1) {
            ctx.addMessage(
                    ValidationMessage.blocking(
                            "T1_NOT_FIRST",
                            "T1 must be first record (line " + lineNumber + ")"
                    )
            );
        }

        if (ctx.isT1Seen()) {
            ctx.addMessage(
                    ValidationMessage.blocking(
                            "DUPLICATE_T1",
                            "Duplicate T1 record at line " + lineNumber
                    )
            );
        }

        ctx.setT1Seen(true);
    }

    private void handleT2(StructuralValidationContext ctx, int lineNumber) {

        if (!ctx.isT1Seen()) {
            ctx.addMessage(
                    ValidationMessage.blocking(
                            "T2_BEFORE_T1",
                            "T2 record before T1 at line " + lineNumber
                    )
            );
        }

        if (ctx.isT2Seen()) {
            ctx.addMessage(
                    ValidationMessage.blocking(
                            "DUPLICATE_T2",
                            "Duplicate T2 record at line " + lineNumber
                    )
            );
        }

        ctx.setT2Seen(true);
    }

    private void handleT3(StructuralValidationContext ctx, int lineNumber) {

        if (!ctx.isT2Seen()) {
            ctx.addMessage(
                    ValidationMessage.blocking(
                            "T3_BEFORE_T2",
                            "T3 record before T2 at line " + lineNumber
                    )
            );
        }
        ctx.incrementT3();
    }

    private void handleT4(StructuralValidationContext ctx, int lineNumber) {

        if (!ctx.isT3Seen()) {
            ctx.addMessage(
                    ValidationMessage.blocking(
                            "T4_BEFORE_T3",
                            "T4 record before any T3 at line " + lineNumber
                    )
            );
        }

        ctx.incrementT4();
    }

    private void handleT5(StructuralValidationContext ctx, int lineNumber) {

        if (ctx.isT5Seen()) {
            ctx.addMessage(
                    ValidationMessage.blocking(
                            "DUPLICATE_T5",
                            "Duplicate T5 record at line " + lineNumber
                    )
            );
        }

        ctx.setT5Seen(true);
    }

    private boolean isPaddingLine(String line) {

        if (line == null || line.isBlank()) {
            return true;
        }

        // Allow lines consisting only of '0'
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) != '0') {
                return false;
            }
        }

        return true;
    }

}
