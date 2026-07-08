package com.codeshare.airline.schedule.ingestion.validation.validator.ssm.structural;

import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.context.SsmIngestionContext;
import com.codeshare.airline.schedule.ingestion.domain.enums.ActionType;
import com.codeshare.airline.schedule.ingestion.domain.enums.ScheduleLineIdentifier;
import com.codeshare.airline.schedule.ingestion.domain.enums.ValidationStage;
import com.codeshare.airline.schedule.ingestion.shared.classifier.LineClassifier;
import com.codeshare.airline.schedule.ingestion.shared.classifier.LineClassifierFactory;
import com.codeshare.airline.schedule.ingestion.shared.util.ActionLineParser;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationResult;
import com.codeshare.airline.schedule.ingestion.validation.validator.StructuralValidation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SsmStructuralValidator implements StructuralValidation<SsmIngestionContext> {

    private static final Set<String> SSM_XASM_ALLOWED = Set.of("SKD", "NEW", "RPL", "CNL");

    @Override
    public Set<MessageType> supportedTypes() {
        return Set.of(MessageType.SSM);
    }

    @Override
    public ValidationResult validate(SsmIngestionContext context) {
        ValidationResult result = new ValidationResult();

        boolean hasHeader = false;
        boolean hasAction = false;
        boolean inBlock = false;
        boolean hasFlight = false;
        boolean hasPeriod = false;
        boolean hasLeg = false;
        boolean hasEquipment = false;
        boolean hasDei = false;

        ActionType currentActionType = null;
        ActionLineParser.ParsedActionLine currentActionLine = null;
        List<String> primaryActions = new ArrayList<>();
        List<String> firstBlockFlights = new ArrayList<>();

        int lineNo = 0;
        boolean endedWithSeparator = false;
        LineClassifier classifier = LineClassifierFactory.create(MessageType.SSM);

        for (String raw : context.getMessageLines()) {
            lineNo++;
            endedWithSeparator = false;

            if (raw != null && raw.length() > 69) {
                result.addError("SSM_010", "Line exceeds 69 printable characters", raw, "LINE " + lineNo, ValidationStage.STRUCTURAL);
            }

            var lc = classifier.classify(raw);
            var type = lc.getType();
            var line = lc.getNormalizedLine();

            if (type == ScheduleLineIdentifier.UNKNOWN) {
                result.addError("SSM_120", "Unknown line", line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
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

                    currentActionLine = ActionLineParser.parseSsm(line);
                    currentActionType = lc.getActionType();
                    primaryActions.add(currentActionLine.primaryAction());
                    validateActionLine(result, currentActionLine, lineNo);

                    hasFlight = false;
                    hasPeriod = false;
                    hasLeg = false;
                    hasEquipment = false;
                    hasDei = false;
                }
                case FLIGHT -> {
                    if (!hasAction) {
                        result.addError("SSM_053", "FLIGHT before ACTION", line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                    }
                    hasFlight = true;
                    if (currentActionLine != null && currentActionLine.primaryAction().equals("SKD") && firstBlockFlights.isEmpty()) {
                        firstBlockFlights.add(line);
                    } else if (currentActionLine != null && currentActionLine.primaryAction().equals("NEW")) {
                        firstBlockFlights.add(line);
                    }
                }
                case PERIOD -> {
                    if (!hasFlight) {
                        result.addError("SSM_062", "PERIOD before FLIGHT", line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                    }
                    hasPeriod = true;
                }
                case LEG -> {
                    if (!hasPeriod) {
                        result.addError("SSM_072", "LEG before PERIOD", line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                    }
                    if (hasEmbeddedTimeCandidate(line) && !isValidTime(line)) {
                        result.addError("SSM_075", "Invalid time format", line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                    }
                    hasLeg = true;
                }
                case TIME -> {
                    if (!hasLeg) {
                        result.addError("SSM_076", "TIME before LEG", line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                    }
                    if (!isValidTime(line)) {
                        result.addError("SSM_075", "Invalid time format", line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                    }
                }
                case EQUIPMENT_AND_SERVICE -> {
                    if (!hasFlight) {
                        result.addError("SSM_081", "EQUIPMENT before FLIGHT", line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                    }
                    hasEquipment = true;
                }
                case DEI -> {
                    if (!hasFlight) {
                        result.addError("SSM_091", "DEI before FLIGHT", line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                    } else {
                        hasDei = true;
                    }
                }
                case SUPPLEMENTARY -> {
                    if (!hasAction) {
                        result.addError("SSM_100", "SI before ACTION", line, "LINE " + lineNo, ValidationStage.STRUCTURAL);
                    }
                }
                case SUB_MESSAGE_SEPARATOR -> {
                    endedWithSeparator = true;
                    validateBlock(result, currentActionType, hasFlight, hasPeriod, hasLeg, hasEquipment, hasDei);
                    inBlock = false;
                    hasFlight = false;
                    hasPeriod = false;
                    hasLeg = false;
                    hasEquipment = false;
                    hasDei = false;
                    currentActionLine = null;
                    currentActionType = null;
                }
                default -> {
                }
            }
        }

        if (!hasHeader) {
            result.addError("SSM_001", "Missing HEADER", null, "HEADER", ValidationStage.STRUCTURAL);
        }

        if (!hasAction) {
            result.addError("SSM_040", "Missing ACTION", null, "ACTION", ValidationStage.STRUCTURAL);
        }

        if (inBlock) {
            if (!endedWithSeparator) {
                result.addError("SSM_110", "Missing final sub-message separator", null, "MESSAGE", ValidationStage.STRUCTURAL);
            }
            validateBlock(result, currentActionType, hasFlight, hasPeriod, hasLeg, hasEquipment, hasDei);
        }

        validateMessageLevelRules(result, primaryActions, firstBlockFlights);

        return result;
    }

    private void validateActionLine(ValidationResult result,
                                    ActionLineParser.ParsedActionLine actionLine,
                                    int lineNo) {
        if (actionLine == null || actionLine.primaryAction() == null || actionLine.primaryAction().isBlank()) {
            result.addError("SSM_041", "Missing primary action identifier", null, "LINE " + lineNo, ValidationStage.STRUCTURAL);
            return;
        }

        if (!actionLine.changeReasons().isEmpty()) {
            result.addError("SSM_042", "Unexpected action modifiers on SSM action line",
                    String.join(" ", actionLine.changeReasons()),
                    "LINE " + lineNo, ValidationStage.STRUCTURAL);
        }

        if (actionLine.asmWithdrawalIndicator() && !SSM_XASM_ALLOWED.contains(actionLine.primaryAction())) {
            result.addError("SSM_047", "XASM is only valid with SKD, NEW, RPL or CNL",
                    actionLine.primaryAction(),
                    "LINE " + lineNo, ValidationStage.STRUCTURAL);
        }
    }

    private void validateMessageLevelRules(ValidationResult result,
                                           List<String> primaryActions,
                                           List<String> flights) {
        if (primaryActions.contains("RSD") && primaryActions.size() > 1) {
            result.addError("SSM_043", "RSD may not be combined with other action identifiers",
                    String.join("/", primaryActions), "MESSAGE", ValidationStage.STRUCTURAL);
        }

        int skdIndex = primaryActions.indexOf("SKD");
        if (skdIndex > 0) {
            result.addError("SSM_044", "SKD must be the first action sub-message in the message",
                    "SKD", "MESSAGE", ValidationStage.STRUCTURAL);
        }

        if (skdIndex >= 0) {
            for (int i = skdIndex + 1; i < primaryActions.size(); i++) {
                if (!"NEW".equals(primaryActions.get(i))) {
                    result.addError("SSM_045", "SKD may only be followed by NEW sub-messages",
                            primaryActions.get(i), "MESSAGE", ValidationStage.STRUCTURAL);
                    break;
                }
            }

            if (flights.size() > 1) {
                String expected = flights.get(0);
                for (int i = 1; i < flights.size(); i++) {
                    if (!expected.equals(flights.get(i))) {
                        result.addError("SSM_046", "SKD and associated NEW sub-messages must use the same flight designator",
                                flights.get(i), "MESSAGE", ValidationStage.STRUCTURAL);
                        break;
                    }
                }
            }
        }
    }

    private void validateBlock(ValidationResult result,
                               ActionType actionType,
                               boolean hasFlight,
                               boolean hasPeriod,
                               boolean hasLeg,
                               boolean hasEquipment,
                               boolean hasDei) {

        if (actionType == null) {
            return;
        }

        if (requiresFlight(actionType) && !hasFlight) {
            result.addError("SSM_050", "Missing FLIGHT in block", null, "BLOCK", ValidationStage.STRUCTURAL);
        }
        if (requiresPeriod(actionType) && !hasPeriod) {
            result.addError("SSM_060", "Missing PERIOD in block", null, "BLOCK", ValidationStage.STRUCTURAL);
        }
        if (requiresLeg(actionType) && !hasLeg) {
            result.addError("SSM_070", "Missing LEG in block", null, "BLOCK", ValidationStage.STRUCTURAL);
        }
        if (requiresEquipment(actionType) && !hasEquipment) {
            result.addError("SSM_080", "Missing EQUIPMENT in block", null, "BLOCK", ValidationStage.STRUCTURAL);
        }
        if (requiresDei(actionType) && !hasDei) {
            result.addError("SSM_090", "Missing DEI in block", null, "BLOCK", ValidationStage.STRUCTURAL);
        }
    }

    private boolean requiresFlight(ActionType actionType) {
        return actionType != ActionType.ACKNOWLEDGED && actionType != ActionType.NOT_ACKNOWLEDGED;
    }

    private boolean requiresPeriod(ActionType actionType) {
        return switch (actionType) {
            case CREATE, DELETE, REPLACE, SCHEDULE_CHANGE, REQUEST_SCHEDULE_DATA,
                    EQUIPMENT_CHANGE, CONFIGURATION_CHANGE, TIME_CHANGE,
                    ADMINISTRATIVE, REVISION, IDENTIFIER_CHANGE -> true;
            default -> false;
        };
    }

    private boolean requiresLeg(ActionType actionType) {
        return switch (actionType) {
            case CREATE, REPLACE, TIME_CHANGE -> true;
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
}
