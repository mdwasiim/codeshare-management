package com.codeshare.airline.ingestion.validations.validator.ssim.structural;

import com.codeshare.airline.ingestion.domain.context.SsimIngestionContext;
import com.codeshare.airline.ingestion.domain.enums.ValidationStage;
import com.codeshare.airline.ingestion.validations.model.ValidationResult;
import com.codeshare.airline.ingestion.validations.validator.StructuralValidation;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SsimStructuralValidator implements StructuralValidation<SsimIngestionContext> {

    private static final int MIN_FILE_LINES = 200;
    private static final int EXPECTED_LENGTH = 200;
    private static final Set<String> VALID_RECORDS = Set.of("1", "2", "3", "4");

    @Override
    public ValidationResult validate(SsimIngestionContext context) {

        ValidationResult result = new ValidationResult();

        /* ================= FILE SANITY ================= */

        if (context.getMessageLines() == null || context.getMessageLines().isEmpty()) {
            result.addError("SSIM_001", "Empty SSIM message", "", "", ValidationStage.STRUCTURAL);
            return result;
        }

        if (context.getMessageLines().size() < MIN_FILE_LINES) {
            result.addError("SSIM_002", "Message too small to be valid SSIM", "", "", ValidationStage.STRUCTURAL);
        }

        /* ================= HEADER ================= */

        String firstLine = context.getMessageLines().getFirst();

        if (firstLine == null || firstLine.isEmpty()) {
            result.addError("SSIM_003", "First line is empty", "", "line:1", ValidationStage.STRUCTURAL);
            return result;
        }

        char firstChar = firstLine.charAt(0);

        if (!Character.isDigit(firstChar)) {
            result.addError(
                    "SSIM_004",
                    "Invalid first record type",
                    firstLine,
                    "line:1",
                    ValidationStage.STRUCTURAL
            );
        }

        /* ================= LINE LOOP ================= */

        int previousRecord = -1;
        int lineNo = 1;

        for (String line : context.getMessageLines()) {

            if (line == null) {
                result.addError(
                        "SSIM_005",
                        "Null line detected",
                        null,
                        "line:" + lineNo,
                        ValidationStage.STRUCTURAL
                );
                lineNo++;
                continue;
            }

            /* ---------- LINE LENGTH ---------- */

            if (line.length() != EXPECTED_LENGTH) {
                result.addError(
                        "SSIM_LEN_001",
                        "Invalid record length (expected 200)",
                        line,
                        "line:" + lineNo,
                        ValidationStage.STRUCTURAL
                );
            }

            /* ---------- MIN LENGTH ---------- */

            if (line.length() < 10) {
                result.addError(
                        "SSIM_006",
                        "Line too short",
                        line,
                        "line:" + lineNo,
                        ValidationStage.STRUCTURAL
                );
            }

            /* ---------- SPACING ---------- */

            if (line.contains("  ")) {
                result.addWarning(
                        "SSIM_007",
                        "Multiple spaces detected",
                        line,
                        "line:" + lineNo,
                        ValidationStage.STRUCTURAL
                );
            }

            /* ---------- RECORD TYPE ---------- */

            String recordType = line.substring(0, 1);

            if (!VALID_RECORDS.contains(recordType)) {
                result.addError(
                        "SSIM_008",
                        "Invalid record type",
                        line,
                        "line:" + lineNo,
                        ValidationStage.STRUCTURAL
                );
            }

            /* ---------- RECORD SEQUENCE ---------- */

            int currentRecord = Character.getNumericValue(recordType.charAt(0));

            if (previousRecord != -1 && currentRecord < previousRecord && currentRecord != 1) {
                result.addError(
                        "SSIM_009",
                        "Invalid record sequence",
                        line,
                        "line:" + lineNo,
                        ValidationStage.STRUCTURAL
                );
            }

            previousRecord = currentRecord;

            lineNo++;
        }

        return result;
    }
}