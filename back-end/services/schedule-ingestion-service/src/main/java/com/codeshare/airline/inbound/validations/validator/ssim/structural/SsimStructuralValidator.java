package com.codeshare.airline.inbound.validations.validator.ssim.structural;

import com.codeshare.airline.inbound.domain.context.SsimIngestionContext;
import com.codeshare.airline.inbound.domain.enums.ValidationStage;
import com.codeshare.airline.inbound.validations.model.ValidationResult;
import com.codeshare.airline.inbound.validations.validator.StructuralValidation;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class SsimStructuralValidator implements StructuralValidation<SsimIngestionContext> {

    private static final int EXPECTED_LENGTH = 200;
    private static final Set<Character> VALID_RECORDS = Set.of('1', '2', '3', '4', '5');

    @Override
    public ValidationResult validate(SsimIngestionContext context) {

        ValidationResult result = new ValidationResult();

        if (context.getMessageLines() == null || context.getMessageLines().isEmpty()) {
            result.addError("SSIM_001", "Empty SSIM message", "", "", ValidationStage.STRUCTURAL);
            return result;
        }

        int lineNo = 1;
        int headerCount = 0;
        int carrierCount = 0;
        int flightCount = 0;
        int trailerCount = 0;
        boolean seenCarrier = false;
        boolean seenFlight = false;
        boolean seenTrailer = false;

        for (String line : context.getMessageLines()) {

            if (line == null) {
                result.addError("SSIM_005", "Null line detected", null, "line:" + lineNo, ValidationStage.STRUCTURAL);
                lineNo++;
                continue;
            }

            if (line.isEmpty()) {
                result.addError("SSIM_006", "Empty line detected", line, "line:" + lineNo, ValidationStage.STRUCTURAL);
                lineNo++;
                continue;
            }

            if (line.length() != EXPECTED_LENGTH) {
                result.addError(
                        "SSIM_LEN_001",
                        "Invalid record length (expected 200)",
                        line,
                        "line:" + lineNo,
                        ValidationStage.STRUCTURAL
                );
            }

            char recordType = line.charAt(0);

            if (recordType == '0') {
                lineNo++;
                continue;
            }

            if (!VALID_RECORDS.contains(recordType)) {
                result.addError(
                        "SSIM_008",
                        "Invalid record type",
                        line,
                        "line:" + lineNo,
                        ValidationStage.STRUCTURAL
                );
                lineNo++;
                continue;
            }

            switch (recordType) {
                case '1' -> {
                    headerCount++;
                    if (seenCarrier || seenFlight || seenTrailer) {
                        result.addError("SSIM_SEQ_001", "Type 1 header must precede carrier, flight, and trailer records", line, "line:" + lineNo, ValidationStage.STRUCTURAL);
                    }
                }
                case '2' -> {
                    carrierCount++;
                    seenCarrier = true;
                    if (headerCount == 0) {
                        result.addError("SSIM_SEQ_002", "Type 2 carrier found before Type 1 header", line, "line:" + lineNo, ValidationStage.STRUCTURAL);
                    }
                    if (seenFlight || seenTrailer) {
                        result.addError("SSIM_SEQ_003", "Type 2 carrier must precede flight and trailer records", line, "line:" + lineNo, ValidationStage.STRUCTURAL);
                    }
                }
                case '3' -> {
                    flightCount++;
                    seenFlight = true;
                    if (!seenCarrier) {
                        result.addError("SSIM_SEQ_004", "Type 3 flight found before Type 2 carrier", line, "line:" + lineNo, ValidationStage.STRUCTURAL);
                    }
                    if (seenTrailer) {
                        result.addError("SSIM_SEQ_005", "Type 3 flight found after Type 5 trailer", line, "line:" + lineNo, ValidationStage.STRUCTURAL);
                    }
                }
                case '4' -> {
                    if (!seenFlight) {
                        result.addError("SSIM_SEQ_006", "Type 4 DEI found before any Type 3 flight", line, "line:" + lineNo, ValidationStage.STRUCTURAL);
                    }
                    if (seenTrailer) {
                        result.addError("SSIM_SEQ_007", "Type 4 DEI found after Type 5 trailer", line, "line:" + lineNo, ValidationStage.STRUCTURAL);
                    }
                }
                case '5' -> {
                    trailerCount++;
                    seenTrailer = true;
                    if (!seenFlight) {
                        result.addError("SSIM_SEQ_008", "Type 5 trailer found before any Type 3 flight", line, "line:" + lineNo, ValidationStage.STRUCTURAL);
                    }
                }
                default -> {
                    // Already checked above.
                }
            }

            lineNo++;
        }

        if (headerCount == 0) {
            result.addError("SSIM_REQ_001", "Missing Type 1 header record", "", "", ValidationStage.STRUCTURAL);
        } else if (headerCount > 1) {
            result.addError("SSIM_REQ_002", "Multiple Type 1 header records", "", "", ValidationStage.STRUCTURAL);
        }

        if (carrierCount == 0) {
            result.addError("SSIM_REQ_003", "Missing Type 2 carrier record", "", "", ValidationStage.STRUCTURAL);
        } else if (carrierCount > 1) {
            result.addError("SSIM_REQ_004", "Multiple Type 2 carrier records", "", "", ValidationStage.STRUCTURAL);
        }

        if (flightCount == 0) {
            result.addError("SSIM_REQ_005", "Missing Type 3 flight records", "", "", ValidationStage.STRUCTURAL);
        }

        if (trailerCount == 0) {
            result.addError("SSIM_REQ_006", "Missing Type 5 trailer record", "", "", ValidationStage.STRUCTURAL);
        } else if (trailerCount > 1) {
            result.addError("SSIM_REQ_007", "Multiple Type 5 trailer records", "", "", ValidationStage.STRUCTURAL);
        }

        return result;
    }
}
