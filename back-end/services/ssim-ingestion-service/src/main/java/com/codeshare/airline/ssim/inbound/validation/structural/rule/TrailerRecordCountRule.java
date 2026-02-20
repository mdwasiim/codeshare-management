package com.codeshare.airline.ssim.inbound.validation.structural.rule;


import com.codeshare.airline.ssim.inbound.validation.model.ValidationMessage;
import com.codeshare.airline.ssim.inbound.domain.contex.StructuralValidationContext;
import org.springframework.stereotype.Component;

@Component
public class TrailerRecordCountRule implements StructuralValidationRule {

    @Override
    public void validate(String line, StructuralValidationContext ctx) {

        if (line == null || line.isEmpty()) {
            return;
        }

        /*if (line.charAt(0) == '5') {

            // Extract declared total record count from fixed position
            // (Adjust byte positions based on SSIM spec)
            String rawCount = line.substring(192, 195).trim();

            try {
                int declaredCount = Integer.parseInt(rawCount);
                ctx.setTrailerDeclaredRecordCount(declaredCount);
            } catch (NumberFormatException ex) {

                ctx.addMessage(
                        ValidationMessage.blocking(
                                "INVALID_T5_COUNT",
                                "Invalid T5 total record count at line "
                                        + ctx.getLineNumber()
                        )
                );
            }
        }*/
        if (line.charAt(0) == '5') {

            Integer declaredCount = extractTrailerCount(line);

            if (declaredCount == null) {

                ctx.addMessage(
                        ValidationMessage.blocking(
                                "INVALID_T5_COUNT",
                                "Unable to extract record count from T5 at line "
                                        + ctx.getLineNumber()
                        )
                );

            } else {
                ctx.setTrailerDeclaredRecordCount(declaredCount);
            }
        }

    }

    @Override
    public void afterFileComplete(StructuralValidationContext ctx) {

        if (ctx.getTrailerDeclaredRecordCount() == null) {

            ctx.addMessage(
                    ValidationMessage.blocking(
                            "MISSING_T5",
                            "Missing T5 trailer record"
                    )
            );
            return;
        }

        /*if (!ctx.getTrailerDeclaredRecordCount()
                .equals(ctx.getRecordsRead())) {

            ctx.addMessage(
                    ValidationMessage.blocking(
                            "T5_COUNT_MISMATCH",
                            "T5 record count mismatch. Declared="
                                    + ctx.getTrailerDeclaredRecordCount()
                                    + ", Actual=" + ctx.getRecordsRead()
                    )
            );
        }*/
    }


    private Integer extractTrailerCount(String line) {

        if (line == null || line.length() < 10) {
            return null;
        }

        // Extract last continuous digit block from right
        int end = line.length() - 1;
        while (end >= 0 && !Character.isDigit(line.charAt(end))) {
            end--;
        }

        int start = end;
        while (start >= 0 && Character.isDigit(line.charAt(start))) {
            start--;
        }

        if (end <= start) {
            return null;
        }

        String numeric = line.substring(start + 1, end + 1);

        try {
            return Integer.parseInt(numeric);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
