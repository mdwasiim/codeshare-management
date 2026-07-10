package com.codeshare.airline.schedule.ingestion.source.camel.channel;

import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.dto.source.AirlineIngestionChannelDTO;
import com.codeshare.airline.schedule.ingestion.dto.source.AirlineIngestionProfileDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

import java.util.UUID;

import static org.apache.camel.builder.Builder.constant;

@Slf4j
public abstract class AbstractChannelRouteBuilder implements ChannelRouteBuilder {

    @Override
    public final void build(RouteBuilder rb, AirlineIngestionProfileDTO profile, AirlineIngestionChannelDTO channel) {

        validate(channel);

        String uri = buildUri(channel);
        String routeId = buildRouteId(profile, channel);

        log.info("Creating route [{}] -> {}", routeId, uri);

        rb.from(uri)
                .routeId(routeId)
                .streamCaching()
                .log("Received file=${header.CamelFileName} | source=" + supports())
                .process(exchange -> {
                    Object body = exchange.getMessage().getBody();
                    log.info("Inbound body type: {}", body != null ? body.getClass() : "NULL");
                })
                .process(this::initializeContext)
                .process(this::beforeProcessing)
                .setHeader("AIRLINE_CODE", constant(profile.getAirlineCode()))
                .setHeader("SOURCE_TYPE", constant(channel.getSourceType().name()))
                .setHeader("MESSAGE_TYPE", constant(channel.getMessageType().name()))
                .log("Forwarding to processing route file=${header.CamelFileName}")
                .toD(resolveProcessingEndpoint(channel.getMessageType()))
                .log("Routed airline=${header.AIRLINE_CODE} type=${header.MESSAGE_TYPE}");
    }

    protected abstract String buildUri(AirlineIngestionChannelDTO channel);

    protected abstract void validate(AirlineIngestionChannelDTO channel);

    protected void beforeProcessing(Exchange exchange) throws Exception {
        // optional override
    }

    private void initializeContext(Exchange exchange) {
        exchange.setProperty("LOAD_ID", UUID.randomUUID());
    }

    protected String buildRouteId(AirlineIngestionProfileDTO profile, AirlineIngestionChannelDTO c) {
        return String.format("INGEST-%s-%s-%s",
                profile.getAirlineCode(),
                c.getSourceType(),
                c.getMessageType());
    }

    protected <T> T val(T value, T def) {
        return value != null ? value : def;
    }

    private String resolveProcessingEndpoint(MessageType messageType) {
        if (messageType == MessageType.SSIM) {
            return "seda:ssim-dataset-processing";
        }
        if (messageType == MessageType.SSM || messageType == MessageType.ASM) {
            return "seda:schedule-message-processing";
        }
        throw new IllegalStateException("Unsupported message type: " + messageType);
    }
}
