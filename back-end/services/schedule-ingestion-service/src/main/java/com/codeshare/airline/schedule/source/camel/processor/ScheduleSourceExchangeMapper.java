package com.codeshare.airline.schedule.source.camel.processor;

import com.codeshare.airline.schedule.domain.common.ScheduleMessageType;
import com.codeshare.airline.schedule.domain.common.ScheduleSourceType;
import com.codeshare.airline.schedule.source.ScheduleSourceFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Component
public class ScheduleSourceExchangeMapper {

    public ScheduleSourceFile map(Exchange exchange) {

        String airlineCode = header(exchange, "AIRLINE_CODE");
        String messageTypeStr = header(exchange, "MESSAGE_TYPE");
        String sourceTypeStr = header(exchange, "SOURCE_TYPE");

        ScheduleMessageType messageType =
                parseMessageType(messageTypeStr);

        ScheduleSourceType sourceType =
                parseSourceType(sourceTypeStr);

        String fileName =
                exchange.getIn()
                        .getHeader(Exchange.FILE_NAME, String.class);

        InputStream bodyStream =
                exchange.getIn().getBody(InputStream.class);

        if (bodyStream == null) {
            throw new IllegalStateException(
                    "Incoming message has no InputStream body");
        }

        // Convert to byte[] to allow safe re-read
        byte[] content = exchange.getContext()
                .getTypeConverter()
                .convertTo(byte[].class, exchange, bodyStream);

        return ScheduleSourceFile.builder()
                .fileId(UUID.randomUUID().toString())
                .fileName(fileName)
                .airlineCode(airlineCode)               // ✅ Added
                .scheduleMessageType(messageType)               // ✅ Added
                .sourceType(sourceType)
                .sourceSystem("CAMEL")
                .receivedAt(Instant.now())
                .streamSupplier(() ->
                        new ByteArrayInputStream(content))  // ✅ Safe reuse
                .build();
    }

    private String header(Exchange exchange, String name) {

        String value =
                exchange.getIn().getHeader(name, String.class);

        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Missing required header: " + name);
        }

        return value;
    }

    private ScheduleMessageType parseMessageType(String value) {

        try {
            return ScheduleMessageType.valueOf(value);
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Invalid MESSAGE_TYPE: " + value);
        }
    }

    private ScheduleSourceType parseSourceType(String value) {

        try {
            return ScheduleSourceType.valueOf(value);
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Invalid SOURCE_TYPE: " + value);
        }
    }
}