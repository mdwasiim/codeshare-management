package com.codeshare.airline.inbound.validations.validator.asm.structural;

import com.codeshare.airline.core.enums.MessageType;
import com.codeshare.airline.inbound.common.classifier.LineClassifier;
import com.codeshare.airline.inbound.common.classifier.LineClassifierFactory;
import com.codeshare.airline.inbound.domain.context.AsmIngestionContext;
import com.codeshare.airline.inbound.domain.enums.ScheduleLineIdentifier;
import com.codeshare.airline.inbound.domain.enums.ValidationStage;
import com.codeshare.airline.inbound.validations.model.ValidationResult;
import com.codeshare.airline.inbound.validations.validator.StructuralValidation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class AsmStructuralValidator implements StructuralValidation<AsmIngestionContext> {

    @Override
    public ValidationResult validate(AsmIngestionContext context) {

        ValidationResult result = new ValidationResult();

        boolean hasHeader = false;
        boolean hasTimeMode = false;
        boolean hasRef = false;

        boolean hasAction = false;
        boolean hasFlight = false;
        boolean hasPeriod = false;
        boolean hasLeg = false;

        boolean inBlock = false;

        Map<String, List<String>> legToDeiMap = new LinkedHashMap<>();

        int lineNo = 0;

        LineClassifier classifier = LineClassifierFactory.create(MessageType.ASM);

        for (String raw : context.getMessageLines()) {

            lineNo++;

            var lc = classifier.classify(raw);
            var type = lc.getType();
            var line = lc.getNormalizedLine();

            if (type == ScheduleLineIdentifier.UNKNOWN) {
                result.addError("ASM_120", "Unknown line",
                        line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                continue;
            }

            switch (type) {

                // ================= HEADER =================
                case HEADER -> hasHeader = true;

                case TIME_MODE -> hasTimeMode = true;

                case MESSAGE_REFERENCE -> hasRef = true;

                // ================= ACTION =================
                case ACTION -> {
                    hasAction = true;
                    inBlock = true;

                    // reset per block
                    hasFlight = false;
                    hasPeriod = false;
                    hasLeg = false;
                    legToDeiMap.clear();
                }

                // ================= FLIGHT =================
                case FLIGHT -> {

                    if (!hasAction) {
                        result.addError("ASM_053", "FLIGHT before ACTION",
                                line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                    }

                    if (hasFlight) {
                        result.addError("ASM_055", "Multiple FLIGHT in same block",
                                line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                    }

                    hasFlight = true;
                    hasLeg = false;
                }

                // ================= PERIOD =================
                case PERIOD -> {

                    if (!hasFlight) {
                        result.addError("ASM_060", "PERIOD before FLIGHT",
                                line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                    }

                    if (hasPeriod) {
                        result.addError("ASM_061", "Duplicate PERIOD",
                                line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                    }

                    hasPeriod = true;
                }

                // ================= LEG =================
                case LEG -> {

                    if (!hasFlight) {
                        result.addError("ASM_073", "LEG before FLIGHT",
                                line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                    }

                    // 🔥 TIME VALIDATION
                    if (!isValidTime(line)) {
                        result.addError("ASM_075", "Invalid time format",
                                line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                    }

                    hasLeg = true;

                    String legKey = extractLegKey(line);
                    legToDeiMap.putIfAbsent(legKey, new ArrayList<>());
                }

                // ================= EQUIPMENT =================
                case EQUIPMENT_AND_SERVICE -> {

                    if (!hasFlight) {
                        result.addError("ASM_081", "EQUIPMENT before FLIGHT",
                                line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                    }
                }

                // ================= DEI =================
                case DEI -> {

                    if (!hasFlight) {
                        result.addError("ASM_091", "DEI before FLIGHT",
                                line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                        break;
                    }

                    String deiLeg = extractDeiLeg(line);

                    if (deiLeg != null && !legToDeiMap.containsKey(deiLeg)) {
                        result.addError("ASM_092", "DEI refers to unknown LEG",
                                line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                    } else if (deiLeg != null) {
                        legToDeiMap.get(deiLeg).add(line);
                    }
                }

                // ================= SUPPLEMENTARY =================
                case SUPPLEMENTARY -> {

                    if (!hasFlight) {
                        result.addError("ASM_100", "SI before FLIGHT",
                                line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                    }
                }

                // ================= SUB MESSAGE =================
                case SUB_MESSAGE_SEPARATOR -> {

                    // 🔥 VALIDATE BLOCK BEFORE RESET
                    validateBlock(result, hasFlight, hasPeriod, hasLeg, legToDeiMap, lineNo);

                    inBlock = false;
                    hasFlight = false;
                    hasPeriod = false;
                    hasLeg = false;
                    legToDeiMap.clear();
                }
            }
        }

        // ================= FINAL CHECK =================

        if (!hasHeader)
            result.addError("ASM_001", "Missing HEADER", null, "HEADER", ValidationStage.STRUCTURAL);

        if (!hasTimeMode)
            result.addError("ASM_002", "Missing TIME MODE", null, "TIME_MODE", ValidationStage.STRUCTURAL);

        if (!hasRef)
            result.addError("ASM_003", "Missing REFERENCE", null, "REF", ValidationStage.STRUCTURAL);

        if (!hasAction)
            result.addError("ASM_040", "Missing ACTION", null, "ACTION", ValidationStage.STRUCTURAL);

        if (inBlock) {
            validateBlock(result, hasFlight, hasPeriod, hasLeg, legToDeiMap, lineNo);
        }

        return result;
    }

    // ================= BLOCK VALIDATION =================

    private void validateBlock(ValidationResult result,
                               boolean hasFlight,
                               boolean hasPeriod,
                               boolean hasLeg,
                               Map<String, List<String>> legToDeiMap,
                               int lineNo) {

        if (!hasFlight) {
            result.addError("ASM_050", "Missing FLIGHT in block", null,
                    "BLOCK", ValidationStage.STRUCTURAL);
        }

        if (!hasPeriod) {
            result.addError("ASM_061", "Missing PERIOD in block", null,
                    "BLOCK", ValidationStage.STRUCTURAL);
        }

        if (!hasLeg) {
            result.addError("ASM_070", "Missing LEG in block", null,
                    "BLOCK", ValidationStage.STRUCTURAL);
        }

        for (Map.Entry<String, List<String>> entry : legToDeiMap.entrySet()) {
            if (entry.getValue().isEmpty()) {
                result.addError("ASM_095", "LEG without DEI",
                        entry.getKey(), "BLOCK", ValidationStage.STRUCTURAL);
            }
        }
    }

    // ================= TIME VALIDATION =================

    private boolean isValidTime(String line) {
        return line.matches(".*\\b([01]\\d|2[0-3])[0-5]\\d\\b.*");
    }

    // ================= LEG KEY =================

    private String extractLegKey(String line) {
        if (line.matches("^[A-Z]{3}\\d{4}.*")) {
            String from = line.substring(0, 3);
            String to = line.substring(line.indexOf(' ') + 1, line.indexOf(' ') + 4);
            return from + "-" + to;
        }

        String[] parts = line.split("\\s+");
        return parts[0] + "-" + parts[1];
    }

    // ================= DEI =================

    private String extractDeiLeg(String line) {

        if (line.matches("^[A-Z]{3}/[A-Z]{3}/.*")) {
            String[] parts = line.split("/");
            return parts[0] + "-" + parts[1];
        }

        if (line.matches("^[A-Z]{3}\\s+[A-Z]{3}.*")) {
            String[] parts = line.split("\\s+");
            return parts[0] + "-" + parts[1];
        }

        return null;
    }
}
