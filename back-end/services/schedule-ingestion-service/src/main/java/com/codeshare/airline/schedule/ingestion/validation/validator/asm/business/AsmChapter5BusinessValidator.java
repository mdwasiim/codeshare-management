package com.codeshare.airline.schedule.ingestion.validation.validator.asm.business;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.context.AsmIngestionContext;
import com.codeshare.airline.schedule.ingestion.domain.enums.ActionType;
import com.codeshare.airline.schedule.ingestion.domain.enums.ScheduleLineIdentifier;
import com.codeshare.airline.schedule.ingestion.domain.enums.ValidationStage;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleFlightDTO;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleLegDTO;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleMessageDTO;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleSubMessageDTO;
import com.codeshare.airline.schedule.ingestion.shared.classifier.LineClassifier;
import com.codeshare.airline.schedule.ingestion.shared.classifier.LineClassifierFactory;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationResult;
import com.codeshare.airline.schedule.ingestion.validation.validator.BusinessValidation;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@Order(1)
public class AsmChapter5BusinessValidator implements BusinessValidation<AsmIngestionContext> {

    @Override
    public Set<MessageType> supportedTypes() {
        return Set.of(MessageType.ASM);
    }

    @Override
    public ValidationResult validate(AsmIngestionContext context) {
        ValidationResult result = new ValidationResult();

        if (context == null || context.getParsedData() == null) {
            return result;
        }

        List<AsmBlock> blocks = splitBlocks(context.getMessageLines());
        ScheduleMessageDTO parsed = context.getParsedData();

        if (parsed.getMessages().size() != blocks.size()) {
            result.addError("ASM_BIZ_000", "Parsed sub-message count does not match source blocks",
                    "MESSAGE", String.valueOf(blocks.size()), ValidationStage.BUSINESS);
            return result;
        }

        boolean hasAck = parsed.getMessages().stream().anyMatch(m -> m.getActionType() == ActionType.ACKNOWLEDGED);
        boolean hasNac = parsed.getMessages().stream().anyMatch(m -> m.getActionType() == ActionType.NOT_ACKNOWLEDGED);
        if ((hasAck || hasNac) && parsed.getMessages().size() > 1) {
            result.addError("ASM_BIZ_001", "ACK/NAC must be the only action sub-message in the message",
                    "MESSAGE", "ACK/NAC", ValidationStage.BUSINESS);
        }

        for (int i = 0; i < parsed.getMessages().size(); i++) {
            validateBlock(result, parsed.getMessages().get(i), blocks.get(i), parsed);
        }

        return result;
    }

    private void validateBlock(ValidationResult result,
                               ScheduleSubMessageDTO message,
                               AsmBlock block,
                               ScheduleMessageDTO envelope) {
        ActionType actionType = message.getActionType();

        switch (actionType) {
            case CREATE -> validateNewOrReplace(result, block, message, "NEW");
            case DELETE -> {
                rejectTypes(result, block, "CNL", Set.of(
                        ScheduleLineIdentifier.TIME,
                        ScheduleLineIdentifier.EQUIPMENT_AND_SERVICE,
                        ScheduleLineIdentifier.DEI,
                        ScheduleLineIdentifier.SUPPLEMENTARY
                ));
            }
            case REINSTATE -> {
                rejectTypes(result, block, "RIN", Set.of(
                        ScheduleLineIdentifier.TIME,
                        ScheduleLineIdentifier.EQUIPMENT_AND_SERVICE,
                        ScheduleLineIdentifier.DEI,
                        ScheduleLineIdentifier.SUPPLEMENTARY
                ));
            }
            case REPLACE -> validateNewOrReplace(result, block, message, "RPL");
            case ACKNOWLEDGED -> {
                rejectTypes(result, block, "ACK", Set.of(
                        ScheduleLineIdentifier.FLIGHT,
                        ScheduleLineIdentifier.LEG,
                        ScheduleLineIdentifier.TIME,
                        ScheduleLineIdentifier.EQUIPMENT_AND_SERVICE,
                        ScheduleLineIdentifier.DEI,
                        ScheduleLineIdentifier.SUPPLEMENTARY
                ));
            }
            case ADMINISTRATIVE -> {
                rejectTypes(result, block, "ADM", Set.of(
                        ScheduleLineIdentifier.TIME,
                        ScheduleLineIdentifier.EQUIPMENT_AND_SERVICE
                ));
                validateNoBlankDeiValues(result, message, block, "ADM");
            }
            case CONFIGURATION_CHANGE -> rejectTypes(result, block, "CON", Set.of(ScheduleLineIdentifier.TIME));
            case EQUIPMENT_CHANGE -> rejectTypes(result, block, "EQT", Set.of(ScheduleLineIdentifier.TIME));
            case IDENTIFIER_CHANGE -> {
                rejectTypes(result, block, "FLT", Set.of(
                        ScheduleLineIdentifier.TIME,
                        ScheduleLineIdentifier.EQUIPMENT_AND_SERVICE
                ));
                if (block.count(ScheduleLineIdentifier.FLIGHT) < 1) {
                    result.addError("ASM_BIZ_020", "FLT requires at least one flight identifier line",
                            block.actionLine, block.recordKey(), ValidationStage.BUSINESS);
                }
            }
            case NOT_ACKNOWLEDGED -> validateNac(result, block, envelope);
            case ROUTING_CHANGE -> {
                if (message.getFlights().stream().flatMap(f -> f.getLegs().stream()).noneMatch(ScheduleLegDTO::hasValidTimes)) {
                    result.addError("ASM_BIZ_030", "RRT requires routing with valid times for uncompleted legs",
                            block.actionLine, block.recordKey(), ValidationStage.BUSINESS);
                }
            }
            case TIME_CHANGE -> {
                rejectTypes(result, block, "TIM", Set.of(ScheduleLineIdentifier.EQUIPMENT_AND_SERVICE));
                if (message.getFlights().stream().flatMap(f -> f.getLegs().stream()).anyMatch(leg -> !leg.hasValidTimes())) {
                    result.addError("ASM_BIZ_040", "TIM requires valid departure and arrival times",
                            block.actionLine, block.recordKey(), ValidationStage.BUSINESS);
                }
            }
            default -> {
            }
        }

        if ((actionType == ActionType.ACKNOWLEDGED || actionType == ActionType.NOT_ACKNOWLEDGED)
                && (envelope.getMessageReference() == null || envelope.getMessageReference().isBlank())) {
            result.addWarning("ASM_BIZ_050", "ACK/NAC should include the original message reference",
                    "HEADER", actionType.name(), ValidationStage.BUSINESS);
        }
    }

    private void validateNewOrReplace(ValidationResult result,
                                      AsmBlock block,
                                      ScheduleSubMessageDTO message,
                                      String actionCode) {
        if (message.getFlights().isEmpty()) {
            result.addError("ASM_BIZ_060", actionCode + " requires flight information",
                    block.actionLine, block.recordKey(), ValidationStage.BUSINESS);
            return;
        }

        for (ScheduleFlightDTO flight : message.getFlights()) {
            if (flight.getOperationDate() == null || flight.getOperationDate().isBlank()) {
                result.addError("ASM_BIZ_061", actionCode + " requires flight identifier date",
                        block.actionLine, block.recordKey(), ValidationStage.BUSINESS);
            }
            for (ScheduleLegDTO leg : flight.getLegs()) {
                if (!leg.hasValidTimes()) {
                    result.addError("ASM_BIZ_062", actionCode + " requires complete timed leg information",
                            block.actionLine, block.recordKey(), ValidationStage.BUSINESS);
                }
            }
        }
    }

    private void validateNoBlankDeiValues(ValidationResult result,
                                          ScheduleSubMessageDTO message,
                                          AsmBlock block,
                                          String actionCode) {
        boolean anyBlankFlightValue = message.getFlights().stream()
                .flatMap(flight -> flight.getDeis().stream())
                .anyMatch(dei -> dei.getValue() == null || dei.getValue().isBlank());
        boolean anyBlankLegValue = message.getFlights().stream()
                .flatMap(flight -> flight.getLegs().stream())
                .flatMap(leg -> leg.getDeis().stream())
                .anyMatch(dei -> dei.getValue() == null || dei.getValue().isBlank());

        if (anyBlankFlightValue || anyBlankLegValue) {
            result.addError("ASM_BIZ_070",
                    actionCode + " DEI replacement values must be present; use NIL when deleting existing data",
                    block.actionLine, block.recordKey(), ValidationStage.BUSINESS);
        }
    }

    private void validateNac(ValidationResult result, AsmBlock block, ScheduleMessageDTO envelope) {
        boolean hasRejectLines = block.rawLines.stream().anyMatch(this::looksLikeRejectLine);
        boolean hasRepeatMessage = block.rawLines.stream().anyMatch(this::looksLikeRepeatedMessageLine);

        if (!hasRejectLines) {
            result.addError("ASM_BIZ_080", "NAC must include reject line number and reason details",
                    block.actionLine, block.recordKey(), ValidationStage.BUSINESS);
        }
        if (!hasRepeatMessage) {
            result.addError("ASM_BIZ_081", "NAC must include repeat of rejected message content",
                    block.actionLine, block.recordKey(), ValidationStage.BUSINESS);
        }
        if (envelope.getMessageReference() == null || envelope.getMessageReference().isBlank()) {
            result.addWarning("ASM_BIZ_082", "NAC should repeat the original message reference",
                    "HEADER", block.recordKey(), ValidationStage.BUSINESS);
        }
    }

    private void rejectTypes(ValidationResult result,
                             AsmBlock block,
                             String actionCode,
                             Set<ScheduleLineIdentifier> disallowedTypes) {
        for (ScheduleLineIdentifier type : disallowedTypes) {
            if (block.count(type) > 0) {
                result.addError("ASM_BIZ_" + businessCodeSuffix(type),
                        actionCode + " may not contain " + type.name() + " lines under Chapter 5",
                        block.actionLine,
                        block.recordKey(),
                        ValidationStage.BUSINESS);
            }
        }
    }

    private String businessCodeSuffix(ScheduleLineIdentifier type) {
        return switch (type) {
            case FLIGHT -> "101";
            case LEG -> "103";
            case TIME -> "104";
            case EQUIPMENT_AND_SERVICE -> "105";
            case DEI -> "106";
            case SUPPLEMENTARY -> "107";
            default -> "199";
        };
    }

    private boolean looksLikeRejectLine(String line) {
        String normalized = line == null ? "" : line.trim().toUpperCase();
        return normalized.matches("^\\d{3}\\s+.+$");
    }

    private boolean looksLikeRepeatedMessageLine(String line) {
        String normalized = line == null ? "" : line.trim().toUpperCase();
        return "ASM".equals(normalized)
                || "UTC".equals(normalized)
                || "LT".equals(normalized)
                || normalized.matches("^\\d{2}[A-Z]{3}\\d{5}[A-Z]\\d{3}.*$")
                || normalized.matches("^(NEW|CNL|RIN|RPL|ACK|ADM|CON|EQT|FLT|NAC|RRT|TIM).*$");
    }

    private List<AsmBlock> splitBlocks(List<String> lines) {
        List<AsmBlock> blocks = new ArrayList<>();
        LineClassifier classifier = LineClassifierFactory.forMessage(MessageType.ASM);
        classifier.reset();

        AsmBlock current = null;
        int lineNo = 0;
        for (String raw : lines) {
            lineNo++;
            var lc = classifier.classify(raw);

            if (current != null && isNacAction(current.actionLine) && lc.getType() != ScheduleLineIdentifier.SUB_MESSAGE_SEPARATOR) {
                current.rawLines.add(raw);
                continue;
            }

            switch (lc.getType()) {
                case ACTION -> {
                    if (current != null) {
                        blocks.add(current);
                    }
                    current = new AsmBlock(lc.getNormalizedLine(), lineNo);
                }
                case SUB_MESSAGE_SEPARATOR -> {
                    if (current != null) {
                        blocks.add(current);
                        current = null;
                    }
                }
                case HEADER, TIME_MODE, MESSAGE_REFERENCE, DATE, HEADER_TIME, PERIOD -> {
                }
                case UNKNOWN -> {
                    if (current != null) {
                        current.rawLines.add(raw);
                    }
                }
                default -> {
                    if (current != null) {
                        current.types.add(lc.getType());
                        current.rawLines.add(raw);
                    }
                }
            }
        }

        if (current != null) {
            blocks.add(current);
        }

        return blocks;
    }

    private boolean isNacAction(String actionLine) {
        return actionLine != null && actionLine.trim().toUpperCase().startsWith("NAC");
    }

    private static final class AsmBlock {
        private final String actionLine;
        private final int actionLineNumber;
        private final List<ScheduleLineIdentifier> types = new ArrayList<>();
        private final List<String> rawLines = new ArrayList<>();

        private AsmBlock(String actionLine, int actionLineNumber) {
            this.actionLine = actionLine;
            this.actionLineNumber = actionLineNumber;
        }

        private long count(ScheduleLineIdentifier type) {
            return types.stream().filter(t -> t == type).count();
        }

        private String recordKey() {
            return "LINE " + actionLineNumber;
        }
    }
}
