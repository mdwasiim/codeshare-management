package com.codeshare.airline.inbound.source.camel.mapper;

import com.codeshare.airline.enums.MessageType;
import com.codeshare.airline.inbound.domain.enums.ProcessingStatus;
import com.codeshare.airline.inbound.domain.enums.SourceType;
import com.codeshare.airline.inbound.source.inbound.ScheduleSourceFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Component
public class ScheduleSourceExchangeMapper {

    public ScheduleSourceFile map(Exchange exchange) {

        String airlineCode = optionalHeader(exchange, "AIRLINE_CODE", "UNK");
        String messageTypeStr = optionalHeader(exchange, "MESSAGE_TYPE", "SSM");
        String sourceTypeStr = optionalHeader(exchange, "SOURCE_TYPE", "LOCAL");

        UUID loadId = exchange.getProperty("LOAD_ID", UUID.class);
        if (loadId == null) {
            loadId = UUID.randomUUID();
            exchange.setProperty("LOAD_ID", loadId);
            log.warn("LOAD_ID missing → generated fallback {}", loadId);
        }

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

            //  compute checksum immediately
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data);

            //String checksum = java.util.HexFormat.of().formatHex(hash);
            String checksum = UUID.randomUUID().toString();
            // optional: store in exchange
            exchange.setProperty("CHECKSUM", checksum);

            UUID fileId = UUID.randomUUID(); // temp ID (checksum later)

            log.debug("Mapped file (streaming+checksum): {}", fileName);

            return ScheduleSourceFile.builder()
                    .fileId(fileId)
                    .loadId(loadId)
                    .fileName(fileName)
                    .airlineCode(airlineCode)
                    .messageType(scheduleType)
                    .sourceType(sourceType)
                    .processingStatus(ProcessingStatus.RECEIVED)

                    //  STREAM SUPPLIER USING DIGEST STREAM
                    .streamSupplier(() -> new ByteArrayInputStream(data))  //  ALWAYS NEW
                    //  store checksum directly
                    .checksum(checksum)
                    .build();

        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize checksum stream", e);
        }
    }

    private String optionalHeader(Exchange exchange, String name, String defaultValue) {
        String value = exchange.getMessage().getHeader(name, String.class);
        if (value == null || value.isBlank()) {
            log.warn("Missing header {} → using default {}", name, defaultValue);
            return defaultValue;
        }
        return value.trim().toUpperCase();
    }

    private MessageType safeParseMessageType(String value) {
        try {
            return MessageType.valueOf(value.toUpperCase());
        } catch (Exception e) {
            log.warn("Invalid MESSAGE_TYPE {} → defaulting to SSM", value);
            return MessageType.SSM;
        }
    }

    private SourceType safeParseSourceType(String value) {
        try {
            return SourceType.valueOf(value.toUpperCase());
        } catch (Exception e) {
            log.warn("Invalid SOURCE_TYPE {} → defaulting to LOCAL", value);
            return SourceType.LOCAL;
        }
    }

    private String firstNonNull(String... values) {
        for (String v : values) {
            if (v != null && !v.isBlank()) return v;
        }
        return null;
    }
}