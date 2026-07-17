package com.codeshare.airline.schedule.ingestion.application.ingest;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class ScheduleMessageTypeResolver {

    public MessageType resolve(MessageType expectedType, String fileName, byte[] content) {
        if (expectedType != null) {
            return expectedType;
        }

        MessageType inferredFromContent = inferFromContent(content);
        if (inferredFromContent != null) {
            return inferredFromContent;
        }

        MessageType inferredFromName = inferFromFileName(fileName);
        if (inferredFromName != null) {
            return inferredFromName;
        }

        throw new IllegalArgumentException("Unable to determine schedule message type");
    }

    public MessageType inferFromContent(byte[] content) {
        if (content == null || content.length == 0) {
            return null;
        }

        String text = new String(content, StandardCharsets.UTF_8);
        String[] lines = text.split("\\R");
        for (String line : lines) {
            if (line == null) {
                continue;
            }

            String normalized = line.trim().toUpperCase();
            if (normalized.isBlank()) {
                continue;
            }

            return switch (normalized) {
                case "ASM" -> MessageType.ASM;
                case "SSM" -> MessageType.SSM;
                case "SSIM" -> MessageType.SSIM;
                default -> null;
            };
        }

        return null;
    }

    public MessageType inferFromFileName(String fileName) {
        if (fileName == null || fileName.isBlank()) {
            return null;
        }

        String normalized = fileName.trim().toUpperCase();
        if (normalized.contains("SSIM")) {
            return MessageType.SSIM;
        }
        if (normalized.contains("SSM")) {
            return MessageType.SSM;
        }
        if (normalized.contains("ASM")) {
            return MessageType.ASM;
        }

        return null;
    }
}
