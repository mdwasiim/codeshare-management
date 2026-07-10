package com.codeshare.airline.schedule.ingestion.source.camel.mapper;

import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.enums.ProcessingStatus;
import com.codeshare.airline.core.enums.schedule.SourceType;
import com.codeshare.airline.schedule.ingestion.source.model.ExchangeConstants;
import com.codeshare.airline.schedule.ingestion.source.model.ScheduleSourceFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.HexFormat;
import java.util.UUID;

@Slf4j
@Component
public class ScheduleSourceExchangeMapper {

    private static final String LOAD_ID = "LOAD_ID";

    public ScheduleSourceFile map(Exchange exchange) {

        String airlineCode = optionalHeader(exchange, "AIRLINE_CODE", "UNK");
        String messageTypeStr = optionalHeader(exchange, "MESSAGE_TYPE", "SSM");
        String sourceTypeStr = optionalHeader(exchange, "SOURCE_TYPE", "LOCAL");

        UUID loadId = resolveLoadId(exchange);
        MessageType scheduleType = safeParseMessageType(messageTypeStr);
        SourceType sourceType = safeParseSourceType(sourceTypeStr);

        String fileName = firstNonNull(
                exchange.getMessage().getHeader(Exchange.FILE_NAME, String.class),
                exchange.getMessage().getHeader("CamelFileName", String.class),
                scheduleType + "_" + Instant.now().toEpochMilli()
        );

        InputStream inputStream = exchange.getMessage().getBody(InputStream.class);
        if (inputStream == null) {
            throw new IllegalStateException("InputStream is null");
        }

        try (InputStream is = inputStream) {
            byte[] data = is.readAllBytes();
            String checksum = sha256(data);

            exchange.setProperty(ExchangeConstants.CHECKSUM, checksum);

            UUID fileId = UUID.randomUUID();

            log.debug("Mapped source file fileName={} checksum={} sizeBytes={}", fileName, checksum, data.length);

            return ScheduleSourceFile.builder()
                    .fileId(fileId)
                    .loadId(loadId)
                    .fileName(fileName)
                    .airlineCode(airlineCode)
                    .messageType(scheduleType)
                    .sourceType(sourceType)
                    .processingStatus(ProcessingStatus.RECEIVED)
                    .fileSizeBytes((long) data.length)
                    .checksum(checksum)
                    .streamSupplier(() -> new ByteArrayInputStream(data))
                    .build();

        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize source stream", e);
        }
    }

    private UUID resolveLoadId(Exchange exchange) {
        Object existing = exchange.getProperty(LOAD_ID);

        if (existing instanceof UUID uuid) {
            return uuid;
        }

        if (existing instanceof String value && !value.isBlank()) {
            try {
                UUID uuid = UUID.fromString(value);
                exchange.setProperty(LOAD_ID, uuid);
                return uuid;
            } catch (IllegalArgumentException ex) {
                log.warn("Invalid LOAD_ID string {}. Generating replacement.", value);
            }
        }

        UUID loadId = UUID.randomUUID();
        exchange.setProperty(LOAD_ID, loadId);
        log.warn("LOAD_ID missing. Generated fallback {}", loadId);
        return loadId;
    }

    private String sha256(byte[] data) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return HexFormat.of().formatHex(digest.digest(data));
    }

    private String optionalHeader(Exchange exchange, String name, String defaultValue) {
        String value = exchange.getMessage().getHeader(name, String.class);
        if (value == null || value.isBlank()) {
            log.warn("Missing header {}. Using default {}", name, defaultValue);
            return defaultValue;
        }
        return value.trim().toUpperCase();
    }

    private MessageType safeParseMessageType(String value) {
        try {
            return MessageType.valueOf(value.toUpperCase());
        } catch (Exception e) {
            log.warn("Invalid MESSAGE_TYPE {}. Defaulting to SSM", value);
            return MessageType.SSM;
        }
    }

    private SourceType safeParseSourceType(String value) {
        try {
            return SourceType.valueOf(value.toUpperCase());
        } catch (Exception e) {
            log.warn("Invalid SOURCE_TYPE {}. Defaulting to LOCAL", value);
            return SourceType.LOCAL;
        }
    }

    private String firstNonNull(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }
}
