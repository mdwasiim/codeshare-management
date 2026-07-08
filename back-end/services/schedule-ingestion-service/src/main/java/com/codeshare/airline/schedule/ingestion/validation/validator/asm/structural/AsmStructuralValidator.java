package com.codeshare.airline.schedule.ingestion.validation.validator.asm.structural;

import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.context.AsmIngestionContext;
import com.codeshare.airline.schedule.ingestion.domain.enums.ActionType;
import com.codeshare.airline.schedule.ingestion.domain.enums.ScheduleLineIdentifier;
import com.codeshare.airline.schedule.ingestion.domain.enums.ValidationStage;
import com.codeshare.airline.schedule.ingestion.shared.classifier.LineClassifier;
import com.codeshare.airline.schedule.ingestion.shared.classifier.LineClassifierFactory;
import com.codeshare.airline.schedule.ingestion.shared.util.ActionLineParser;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationResult;
import com.codeshare.airline.schedule.ingestion.validation.validator.StructuralValidation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class AsmStructuralValidator implements StructuralValidation<AsmIngestionContext> {

    private static final Set<String> ASM_REASON_REQUIRED = Set.of("NEW", "CNL", "RIN", "RPL", "ADM", "CON", "EQT", "RRT", "TIM");
    private static final Set<String> ASM_SINGLE_ONLY = Set.of("NEW", "CNL", "RIN", "FLT", "ADM");

    @Override
    public Set<MessageType> supportedTypes() {
        return Set.of(MessageType.ASM);
    }

    @Override
    public ValidationResult validate(AsmIngestionContext context) {
        ValidationResult result = new ValidationResult();

        boolean hasHeader = false;
        boolean hasAction = false;
        boolean inBlock = false;
        boolean hasFlight = false;
        boolean hasLeg = false;
        boolean hasEquipment = false;
        boolean hasDei = false;

        ActionType currentActionType = null;
        ActionLineParser.ParsedActionLine currentActionLine = null;
        int lineNo = 0;
        boolean endedWithSeparator = false;

        LineClassifier classifier = LineClassifierFactory.forMessage(MessageType.ASM);

        for (String raw : context.getMessageLines()) {
            lineNo++;
            endedWithSeparator = false;

            var lc = classifier.classify(raw);
            var type = lc.getType();
            var line = lc.getNormalizedLine();

            if (currentActionType == ActionType.NOT_ACKNOWLEDGED && type != ScheduleLineIdentifier.SUB_MESSAGE_SEPARATOR) {
                if (type == ScheduleLineIdentifier.UNKNOWN && !looksLikeAsmNacRejectLine(raw)) {
                    result.addError("ASM_120", "Unknown line", line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                }
                continue;
            }

            if (type == ScheduleLineIdentifier.UNKNOWN) {
                result.addError("ASM_120", "Unknown line", line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                continue;
            }

            switch (type) {
                case HEADER -> hasHeader = true;
                case TIME_MODE, MESSAGE_REFERENCE -> {
                    // optional
                }
                case ACTION -> {
                    hasAction = true;
                    inBlock = true;

                    currentActionLine = ActionLineParser.parseAsm(line);
                    currentActionType = lc.getActionType();
                    validateActionLine(result, currentActionLine, lineNo);

                    hasFlight = false;
                    hasLeg = false;
                    hasEquipment = false;
                    hasDei = false;
                }
                case FLIGHT -> {
                    if (!hasAction) {
                        result.addError("ASM_053", "FLIGHT before ACTION", line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                    }
                    hasFlight = true;
                }
                case LEG -> {
                    if (!hasFlight) {
                        result.addError("ASM_073", "LEG before FLIGHT", line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                    }
                    if (hasEmbeddedTimeCandidate(line) && !isValidTime(line)) {
                        result.addError("ASM_075", "Invalid time format", line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                    }
                    hasLeg = true;
                }
                case TIME -> {
                    if (!hasLeg) {
                        result.addError("ASM_076", "TIME before LEG", line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                    }
                    if (!isValidTime(line)) {
                        result.addError("ASM_075", "Invalid time format", line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                    }
                }
                case EQUIPMENT_AND_SERVICE -> {
                    if (!hasFlight) {
                        result.addError("ASM_081", "EQUIPMENT before FLIGHT", line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                    }
                    hasEquipment = true;
                }
                case DEI -> {
                    if (!hasFlight) {
                        result.addError("ASM_091", "DEI before FLIGHT", line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                        break;
                    }
                    hasDei = true;
                }
                case SUPPLEMENTARY -> {
                    if (!hasAction) {
                        result.addError("ASM_100", "SI before ACTION", line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                    }
                }
                case SUB_MESSAGE_SEPARATOR -> {
                    endedWithSeparator = true;
                    validateBlock(result, currentActionType, hasFlight, hasLeg, hasEquipment, hasDei);
                    inBlock = false;
                    hasFlight = false;
                    hasLeg = false;
                    hasEquipment = false;
                    hasDei = false;
                    currentActionType = null;
                    currentActionLine = null;
                }
                default -> {
                }
            }
        }

        if (!hasHeader) {
            result.addError("ASM_001", "Missing HEADER", null, "HEADER", ValidationStage.STRUCTURAL);
        }

        if (!hasAction) {
            result.addError("ASM_040", "Missing ACTION", null, "ACTION", ValidationStage.STRUCTURAL);
        }

        if (inBlock) {
            if (!endedWithSeparator) {
                result.addError("ASM_110", "Missing final sub-message separator", null, "MESSAGE", ValidationStage.STRUCTURAL);
            }
            validateBlock(result, currentActionType, hasFlight, hasLeg, hasEquipment, hasDei);
        }

        return result;
    }

    private void validateActionLine(ValidationResult result,
                                    ActionLineParser.ParsedActionLine actionLine,
                                    int lineNo) {
        String primary = actionLine.primaryAction();

        if (primary == null || primary.isBlank()) {
            result.addError("ASM_041", "Missing primary action identifier", null, "LINE " + lineNo, ValidationStage.STRUCTURAL);
            return;
        }

        if (!ASM_SINGLE_ONLY.contains(primary) && !actionLine.secondaryActions().isEmpty()) {
            if (!areAllowedSecondaryActions(primary, actionLine.secondaryActions())) {
                result.addError("ASM_042", "Unsupported secondary action combination",
                        primary + "/" + String.join("/", actionLine.secondaryActions()),
                        "LINE " + lineNo, ValidationStage.STRUCTURAL);
            }
        } else if (ASM_SINGLE_ONLY.contains(primary) && !actionLine.secondaryActions().isEmpty()) {
            result.addError("ASM_043", "Secondary action not permitted for primary action",
                    primary + "/" + String.join("/", actionLine.secondaryActions()),
                    "LINE " + lineNo, ValidationStage.STRUCTURAL);
        }

        if (("ACK".equals(primary) || "NAC".equals(primary)) && !actionLine.secondaryActions().isEmpty()) {
            result.addError("ASM_044", "Secondary action not permitted for ACK/NAC",
                    primary + "/" + String.join("/", actionLine.secondaryActions()),
                    "LINE " + lineNo, ValidationStage.STRUCTURAL);
        }
    }

    private boolean areAllowedSecondaryActions(String primary, List<String> secondary) {
        Set<String> allowed = switch (primary) {
            case "RPL" -> Set.of("ADM", "CON", "EQT", "RRT", "TIM");
            case "CON" -> Set.of("ADM");
            case "EQT" -> Set.of("ADM", "CON");
            case "RRT" -> Set.of("ADM", "CON", "EQT", "TIM");
            case "TIM" -> Set.of("ADM");
            default -> Set.of();
        };
        return secondary.stream().allMatch(allowed::contains);
    }

    private void validateBlock(ValidationResult result,
                               ActionType actionType,
                               boolean hasFlight,
                               boolean hasLeg,
                               boolean hasEquipment,
                               boolean hasDei) {

        if (actionType == null) {
            return;
        }

        if (requiresFlight(actionType) && !hasFlight) {
            result.addError("ASM_050", "Missing FLIGHT in block", null, "BLOCK", ValidationStage.STRUCTURAL);
        }

        if (requiresLeg(actionType) && !hasLeg) {
            result.addError("ASM_070", "Missing LEG in block", null, "BLOCK", ValidationStage.STRUCTURAL);
        }

        if (requiresEquipment(actionType) && !hasEquipment) {
            result.addError("ASM_080", "Missing EQUIPMENT in block", null, "BLOCK", ValidationStage.STRUCTURAL);
        }

        if (requiresDei(actionType) && !hasDei) {
            result.addError("ASM_090", "Missing DEI in block", null, "BLOCK", ValidationStage.STRUCTURAL);
        }
    }

    private boolean requiresFlight(ActionType actionType) {
        return actionType != ActionType.ACKNOWLEDGED && actionType != ActionType.NOT_ACKNOWLEDGED;
    }

    private boolean requiresLeg(ActionType actionType) {
        return switch (actionType) {
            case CREATE, REPLACE, TIME_CHANGE, ROUTING_CHANGE -> true;
            default -> false;
        };
    }

    private boolean requiresEquipment(ActionType actionType) {
        return switch (actionType) {
            case CREATE, REPLACE, EQUIPMENT_CHANGE, CONFIGURATION_CHANGE -> true;
            default -> false;
        };
    }

    private boolean requiresDei(ActionType actionType) {
        return actionType == ActionType.ADMINISTRATIVE;
    }

    private boolean isValidTime(String line) {
        return line.matches(".*\\b([01]\\d|2[0-3])[0-5]\\d\\b.*");
    }

    private boolean hasEmbeddedTimeCandidate(String line) {
        String[] parts = line.split("\\s+");
        return parts.length >= 4 && (parts[2].matches("\\d{4}.*") || parts[3].matches("\\d{4}.*"));
    }

    private boolean looksLikeAsmNacRejectLine(String raw) {
        if (raw == null) {
            return false;
        }
        String normalized = raw.trim().toUpperCase();
        return normalized.matches("^\\d{3}(\\s+.+)?$")
                || normalized.matches("^[A-Z0-9./<\\- ]+$");
    }
}
