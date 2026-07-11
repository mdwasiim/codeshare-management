package com.codeshare.airline.schedule.ingestion.extraction.extractor;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.enums.ActionType;
import com.codeshare.airline.schedule.ingestion.persistence.mappers.schedule.ActionTypeMapper;

import java.util.List;
import java.util.function.Consumer;

public class MessageExtractorUtil {

    // 3. Message reference line
    private static String MESSAGE_REFERENCE = "^\\d{2}[A-Z]{3}\\d+E\\d+/REF\\s+\\d+/\\d+$";


    public static void flush(List<String> current, Consumer<List<String>> consumer) {
        if (!current.isEmpty()) {
            consumer.accept(List.copyOf(current));
            current.clear();
        }
    }

    public static boolean isValidSubMessage(List<String> lines, MessageType messageType) {

        return lines.stream()
                .anyMatch(line ->
                        resolveActionType(messageType, line.trim())
                                != ActionType.UNKNOWN
                );
    }

    public  static boolean isHeaderLine(String line, MessageType type) {

        String normalized = line.trim().toUpperCase();

        // 1. Message type (SSM / ASM)
        if (normalized.equals(type.name())) {
            return true;
        }

        // 2. Time mode (LT / UTC)
        if (normalized.equals("LT") || normalized.equals("UTC")) {
            return true;
        }

        // 3. Message reference line
        if (normalized.matches(MESSAGE_REFERENCE)) {
            return true;
        }

        return false;
    }

    // =========================
    //  ACTION TYPE
    // =========================

    public static ActionType resolveActionType(MessageType messageType, String line) {
        String token = extractFirstToken(line);
        return switch (messageType) {
            case SSM -> ActionTypeMapper.fromSsm(token);
            case ASM -> ActionTypeMapper.fromAsm(token);
            case SSIM -> ActionType.BASELINE_LOAD;
        };
    }

    public static String extractFirstToken(String line) {
        int idx = line.indexOf(' ');
        return idx > 0 ? line.substring(0, idx) : line;
    }
}
