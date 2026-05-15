package com.codeshare.airline.inbound.validations.validator.ssm.structural;

import com.codeshare.airline.core.enums.MessageType;
import com.codeshare.airline.inbound.common.classifier.LineClassifier;
import com.codeshare.airline.inbound.common.classifier.LineClassifierFactory;
import com.codeshare.airline.inbound.domain.context.SsmIngestionContext;
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
import java.util.Set;

@Component
@AllArgsConstructor
public class SsmStructuralValidator implements StructuralValidation<SsmIngestionContext> {

    @Override
    public Set<MessageType> supportedTypes() {
        return Set.of(MessageType.SSM);
    }

    @Override
    public ValidationResult validate(SsmIngestionContext context) {

        ValidationResult result = new ValidationResult();

        // ================= HEADER =================
        boolean hasHeader = false;
        boolean hasTimeMode = false;
        boolean hasRef = false;

        // ================= BLOCK STATE =================
        boolean hasAction = false;
        boolean hasFlight = false;
        boolean hasPeriod = false;
        boolean hasLeg = false;
        boolean hasEquipment = false;
        boolean inBlock = false;

        Map<String, List<String>> legToDeiMap = new LinkedHashMap<>();

        int lineNo = 0;

        LineClassifier classifier = LineClassifierFactory.create(MessageType.SSM);

        for (String raw : context.getMessageLines()) {

            lineNo++;

            var lc = classifier.classify(raw);
            var type = lc.getType();
            var line = lc.getNormalizedLine();

            if (type == ScheduleLineIdentifier.UNKNOWN) {
                result.addError("SSM_120", "Unknown line",
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

                    // reset block state
                    hasFlight = false;
                    hasPeriod = false;
                    hasLeg = false;
                    hasEquipment = false;
                    legToDeiMap.clear();
                }

                // ================= FLIGHT =================
                case FLIGHT -> {

                    if (!hasAction) {
                        result.addError("SSM_053", "FLIGHT before ACTION",
                                line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                    }

                    if (hasFlight) {
                        result.addError("SSM_055", "Multiple FLIGHT in same block",
                                line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                    }

                    hasFlight = true;
                }

                // ================= PERIOD =================
                case PERIOD -> {

                    if (!hasFlight) {
                        result.addError("SSM_062", "PERIOD before FLIGHT",
                                line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                    }

                    if (hasPeriod) {
                        result.addError("SSM_061", "Duplicate PERIOD",
                                line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                    }

                    hasPeriod = true;
                }

                // ================= LEG =================
                case LEG -> {

                    if (!hasPeriod) {
                        result.addError("SSM_072", "LEG before PERIOD",
                                line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                    }

                    if (hasEmbeddedTimeCandidate(line) && !isValidTime(line)) {
                        result.addError("SSM_075", "Invalid time format",
                                line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                    }

                    hasLeg = true;

                    String legKey = extractLegKey(line);
                    legToDeiMap.putIfAbsent(legKey, new ArrayList<>());
                }

                case TIME -> {
                    if (!hasLeg) {
                        result.addError("SSM_076", "TIME before LEG",
                                line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                    }

                    if (!isValidTime(line)) {
                        result.addError("SSM_075", "Invalid time format",
                                line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                    }
                }

                // ================= EQUIPMENT =================
                case EQUIPMENT_AND_SERVICE -> {

                    if (!hasPeriod) {
                        result.addError("SSM_081", "EQUIPMENT before PERIOD",
                                line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                    }

                    if (hasEquipment) {
                        result.addError("SSM_082", "Duplicate EQUIPMENT",
                                line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                    }

                    hasEquipment = true;
                }

                // ================= DEI =================
                case DEI -> {

                    if (!hasLeg) {
                        result.addError("SSM_091", "DEI before LEG",
                                line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                        break;
                    }

                    String deiLeg = extractDeiLeg(line);

                    if (deiLeg == null) {
                        continue;
                    }

                    if (!legToDeiMap.containsKey(deiLeg)) {
                        result.addError("SSM_092", "DEI refers to unknown LEG",
                                line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                        break;
                    }

                    legToDeiMap.get(deiLeg).add(line);
                }

                // ================= SUPPLEMENTARY =================
                case SUPPLEMENTARY -> {
                    if (!hasFlight) {
                        result.addError("SSM_100", "SI before FLIGHT",
                                line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                    }
                }

                // ================= SUB MESSAGE =================
                case SUB_MESSAGE_SEPARATOR -> {

                    validateBlock(result, hasFlight, hasPeriod, hasLeg, legToDeiMap);

                    // reset block
                    inBlock = false;
                    hasFlight = false;
                    hasPeriod = false;
                    hasLeg = false;
                    hasEquipment = false;
                    legToDeiMap.clear();
                }
            }
        }

        // ================= FINAL CHECK =================

        if (!hasHeader)
            result.addError("SSM_001", "Missing HEADER", null, "HEADER", ValidationStage.STRUCTURAL);

        if (!hasTimeMode)
            result.addError("SSM_002", "Missing TIME MODE", null, "TIME_MODE", ValidationStage.STRUCTURAL);

        if (!hasRef)
            result.addError("SSM_003", "Missing REFERENCE", null, "REF", ValidationStage.STRUCTURAL);

        if (!hasAction)
            result.addError("SSM_040", "Missing ACTION", null, "ACTION", ValidationStage.STRUCTURAL);

        if (inBlock) {
            validateBlock(result, hasFlight, hasPeriod, hasLeg, legToDeiMap);
        }

        return result;
    }

    // ================= BLOCK VALIDATION =================

    private void validateBlock(ValidationResult result,
                               boolean hasFlight,
                               boolean hasPeriod,
                               boolean hasLeg,
                               Map<String, List<String>> legToDeiMap) {

        if (!hasFlight) {
            result.addError("SSM_050", "Missing FLIGHT in block", null,
                    "BLOCK", ValidationStage.STRUCTURAL);
        }

        if (!hasPeriod) {
            result.addError("SSM_060", "Missing PERIOD in block", null,
                    "BLOCK", ValidationStage.STRUCTURAL);
        }

        if (!hasLeg) {
            result.addError("SSM_070", "Missing LEG in block", null,
                    "BLOCK", ValidationStage.STRUCTURAL);
        }

    }

    // ================= TIME VALIDATION =================

    private boolean isValidTime(String line) {
        return line.matches(".*\\b([01]\\d|2[0-3])[0-5]\\d\\b.*");
    }

    private boolean hasEmbeddedTimeCandidate(String line) {
        String[] parts = line.split("\\s+");
        return parts.length >= 4
                && (parts[2].matches("\\d{4}.*") || parts[3].matches("\\d{4}.*"));
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
