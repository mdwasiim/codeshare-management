package com.codeshare.airline.schedule.ingestion.validation.validator.ssm.business;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.context.SsmIngestionContext;
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
public class SsmChapter4BusinessValidator implements BusinessValidation<SsmIngestionContext> {

    @Override
    public Set<MessageType> supportedTypes() {
        return Set.of(MessageType.SSM);
    }

    @Override
    public ValidationResult validate(SsmIngestionContext context) {
        ValidationResult result = new ValidationResult();

        if (context == null || context.getParsedData() == null) {
            return result;
        }

        List<SsmBlock> blocks = splitBlocks(context.getMessageLines());
        ScheduleMessageDTO parsed = context.getParsedData();

        if (parsed.getMessages().size() != blocks.size()) {
            result.addError("SSM_BIZ_000", "Parsed sub-message count does not match source blocks",
                    "MESSAGE", String.valueOf(blocks.size()), ValidationStage.BUSINESS);
            return result;
        }

        boolean hasAck = parsed.getMessages().stream().anyMatch(m -> m.getActionType() == ActionType.ACKNOWLEDGED);
        boolean hasNac = parsed.getMessages().stream().anyMatch(m -> m.getActionType() == ActionType.NOT_ACKNOWLEDGED);
        if ((hasAck || hasNac) && parsed.getMessages().size() > 1) {
            result.addError("SSM_BIZ_001", "ACK/NAC must be the only action sub-message in the message",
                    "MESSAGE", "ACK/NAC", ValidationStage.BUSINESS);
        }

        for (int i = 0; i < parsed.getMessages().size(); i++) {
            validateBlock(result, parsed.getMessages().get(i), blocks.get(i), parsed);
        }

        return result;
    }

    private void validateBlock(ValidationResult result,
                               ScheduleSubMessageDTO message,
                               SsmBlock block,
                               ScheduleMessageDTO envelope) {
        ActionType actionType = message.getActionType();

        if (actionType != ActionType.ACKNOWLEDGED && actionType != ActionType.NOT_ACKNOWLEDGED
                && message.getFlights().size() != 1) {
            result.addError("SSM_BIZ_010", "Each SSM action sub-message must contain exactly one flight",
                    block.actionLine, block.recordKey(), ValidationStage.BUSINESS);
        }

        switch (actionType) {
            case CREATE -> validateCompleteFlightChange(result, block, message, "NEW");
            case REPLACE -> validateCompleteFlightChange(result, block, message, "RPL");
            case DELETE -> {
                rejectTypes(result, block, "CNL", Set.of(
                        ScheduleLineIdentifier.LEG,
                        ScheduleLineIdentifier.TIME,
                        ScheduleLineIdentifier.EQUIPMENT_AND_SERVICE,
                        ScheduleLineIdentifier.DEI
                ));
            }
            case SCHEDULE_CHANGE -> {
                rejectTypes(result, block, "SKD", Set.of(
                        ScheduleLineIdentifier.LEG,
                        ScheduleLineIdentifier.TIME,
                        ScheduleLineIdentifier.EQUIPMENT_AND_SERVICE,
                        ScheduleLineIdentifier.DEI,
                        ScheduleLineIdentifier.SUPPLEMENTARY
                ));
            }
            case ACKNOWLEDGED -> {
                rejectTypes(result, block, "ACK", Set.of(
                        ScheduleLineIdentifier.FLIGHT,
                        ScheduleLineIdentifier.PERIOD,
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
                if (message.getFlights().size() == 1) {
                    validateDeiPayload(result, message.getFlights().getFirst(), block, true);
                }
            }
            case CONFIGURATION_CHANGE -> rejectTypes(result, block, "CON", Set.of(ScheduleLineIdentifier.TIME));
            case EQUIPMENT_CHANGE -> rejectTypes(result, block, "EQT", Set.of(ScheduleLineIdentifier.TIME));
            case IDENTIFIER_CHANGE -> rejectTypes(result, block, "FLT", Set.of(
                    ScheduleLineIdentifier.LEG,
                    ScheduleLineIdentifier.TIME,
                    ScheduleLineIdentifier.EQUIPMENT_AND_SERVICE
            ));
            case NOT_ACKNOWLEDGED -> {
                rejectTypes(result, block, "NAC", Set.of(
                        ScheduleLineIdentifier.FLIGHT,
                        ScheduleLineIdentifier.PERIOD,
                        ScheduleLineIdentifier.LEG,
                        ScheduleLineIdentifier.TIME,
                        ScheduleLineIdentifier.EQUIPMENT_AND_SERVICE,
                        ScheduleLineIdentifier.DEI
                ));
                if (message.getSupplementaryInfo() == null || message.getSupplementaryInfo().isEmpty()) {
                    result.addError("SSM_BIZ_020", "NAC must include reject reason text",
                            block.actionLine, block.recordKey(), ValidationStage.BUSINESS);
                } else if (message.getSupplementaryInfo().stream().noneMatch(this::containsLineReference)) {
                    result.addError("SSM_BIZ_021", "NAC should include original line reference(s)",
                            block.actionLine, block.recordKey(), ValidationStage.BUSINESS);
                }
            }
            case REVISION -> {
                rejectTypes(result, block, "REV", Set.of(
                        ScheduleLineIdentifier.LEG,
                        ScheduleLineIdentifier.TIME,
                        ScheduleLineIdentifier.EQUIPMENT_AND_SERVICE,
                        ScheduleLineIdentifier.DEI
                ));
                if (message.getFlights().size() == 1 && message.getFlights().getFirst().getPeriods().size() < 2) {
                    result.addError("SSM_BIZ_030", "REV must state existing and revised period/day information",
                            block.actionLine, block.recordKey(), ValidationStage.BUSINESS);
                }
            }
            case REQUEST_SCHEDULE_DATA -> rejectTypes(result, block, "RSD", Set.of(
                    ScheduleLineIdentifier.LEG,
                    ScheduleLineIdentifier.TIME,
                    ScheduleLineIdentifier.EQUIPMENT_AND_SERVICE,
                    ScheduleLineIdentifier.DEI,
                    ScheduleLineIdentifier.SUPPLEMENTARY
            ));
            case TIME_CHANGE -> {
                rejectTypes(result, block, "TIM", Set.of(ScheduleLineIdentifier.EQUIPMENT_AND_SERVICE));
                if (message.getFlights().size() == 1) {
                    for (ScheduleLegDTO leg : message.getFlights().getFirst().getLegs()) {
                        if (!leg.hasValidTimes()) {
                            result.addError("SSM_BIZ_040", "TIM requires valid departure and arrival times for each leg",
                                    block.actionLine, block.recordKey(), ValidationStage.BUSINESS);
                        }
                    }
                }
            }
            default -> {
            }
        }

        if ((actionType == ActionType.ACKNOWLEDGED || actionType == ActionType.NOT_ACKNOWLEDGED)
                && (envelope.getMessageReference() == null || envelope.getMessageReference().isBlank())) {
            result.addWarning("SSM_BIZ_050", "ACK/NAC should include the original message reference",
                    "HEADER", actionType.name(), ValidationStage.BUSINESS);
        }
    }

    private void validateCompleteFlightChange(ValidationResult result,
                                              SsmBlock block,
                                              ScheduleSubMessageDTO message,
                                              String actionCode) {
        if (message.getFlights().size() != 1) {
            return;
        }

        ScheduleFlightDTO flight = message.getFlights().getFirst();
        for (ScheduleLegDTO leg : flight.getLegs()) {
            if (!leg.hasValidTimes()) {
                result.addError("SSM_BIZ_060", actionCode + " requires complete timed leg information",
                        block.actionLine, block.recordKey(), ValidationStage.BUSINESS);
            }
        }
    }

    private void validateDeiPayload(ValidationResult result,
                                    ScheduleFlightDTO flight,
                                    SsmBlock block,
                                    boolean nilAllowed) {
        boolean anyBlankValue = flight.getDeis().stream().anyMatch(dei -> dei.getValue() == null || dei.getValue().isBlank());
        boolean anyBlankLegValue = flight.getLegs().stream()
                .flatMap(leg -> leg.getDeis().stream())
                .anyMatch(dei -> dei.getValue() == null || dei.getValue().isBlank());

        if (anyBlankValue || anyBlankLegValue) {
            result.addError("SSM_BIZ_070",
                    nilAllowed ? "ADM DEI replacement values must be present; use NIL when deleting existing data"
                            : "DEI values must not be blank",
                    block.actionLine, block.recordKey(), ValidationStage.BUSINESS);
        }
    }

    private void rejectTypes(ValidationResult result,
                             SsmBlock block,
                             String actionCode,
                             Set<ScheduleLineIdentifier> disallowedTypes) {
        for (ScheduleLineIdentifier type : disallowedTypes) {
            if (block.count(type) > 0) {
                result.addError("SSM_BIZ_" + businessCodeSuffix(type),
                        actionCode + " may not contain " + type.name() + " lines under Chapter 4",
                        block.actionLine,
                        block.recordKey(),
                        ValidationStage.BUSINESS);
            }
        }
    }

    private String businessCodeSuffix(ScheduleLineIdentifier type) {
        return switch (type) {
            case FLIGHT -> "101";
            case PERIOD -> "102";
            case LEG -> "103";
            case TIME -> "104";
            case EQUIPMENT_AND_SERVICE -> "105";
            case DEI -> "106";
            case SUPPLEMENTARY -> "107";
            default -> "199";
        };
    }

    private boolean containsLineReference(String text) {
        return text != null && text.matches(".*\\d+.*");
    }

    private List<SsmBlock> splitBlocks(List<String> lines) {
        List<SsmBlock> blocks = new ArrayList<>();
        LineClassifier classifier = LineClassifierFactory.forMessage(MessageType.SSM);
        classifier.reset();

        SsmBlock current = null;
        int lineNo = 0;
        for (String raw : lines) {
            lineNo++;
            var lc = classifier.classify(raw);
            switch (lc.getType()) {
                case ACTION -> {
                    if (current != null) {
                        blocks.add(current);
                    }
                    current = new SsmBlock(lc.getNormalizedLine(), lineNo);
                }
                case SUB_MESSAGE_SEPARATOR -> {
                    if (current != null) {
                        blocks.add(current);
                        current = null;
                    }
                }
                case HEADER, TIME_MODE, MESSAGE_REFERENCE, DATE, HEADER_TIME, UNKNOWN -> {
                }
                default -> {
                    if (current != null) {
                        current.types.add(lc.getType());
                    }
                }
            }
        }

        if (current != null) {
            blocks.add(current);
        }

        return blocks;
    }

    private static final class SsmBlock {
        private final String actionLine;
        private final int actionLineNumber;
        private final List<ScheduleLineIdentifier> types = new ArrayList<>();

        private SsmBlock(String actionLine, int actionLineNumber) {
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
