package com.codeshare.airline.inbound.source.camel.channel;

import com.codeshare.airline.inbound.entities.source.ScheduleIngestionChannelEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

import java.util.UUID;

import static org.apache.camel.builder.Builder.constant;

@Slf4j
public abstract class AbstractChannelRouteBuilder implements ChannelRouteBuilder {

    @Override
    public final void build(RouteBuilder rb, ScheduleIngestionChannelEntity channel) {

        validate(channel);

        String uri = buildUri(channel);
        String routeId = buildRouteId(channel);

        log.info(" Creating route [{}] → {}", routeId, uri);

        rb.from(uri)
                .routeId(routeId)

                // 🔥 VERY IMPORTANT (FIX STREAM ISSUE)
                .streamCaching()

                /* =========================
                   INBOUND LOGGING
                   ========================= */
                .log("📥 Received file=${header.CamelFileName} | source=" + supports())

                /* =========================
                   DEBUG BODY
                   ========================= */
                .process(exchange -> {
                    Object body = exchange.getMessage().getBody();
                    log.info("📦 Body type: {}", body != null ? body.getClass() : "NULL");
                })

                /* =========================
                   CONTEXT INIT
                   ========================= */
                .process(this::initializeContext)

                /* =========================
                   EXTENSION HOOK
                   ========================= */
                .process(this::beforeProcessing)

                /* =========================
                   HEADERS
                   ========================= */
                .setHeader("AIRLINE_CODE", constant(channel.getProfile().getAirlineCode()))
                .setHeader("SOURCE_TYPE", constant(channel.getSourceType().name()))
                .setHeader("MESSAGE_TYPE", constant(channel.getMessageType().name()))

                /* =========================
                   FORWARD
                   ========================= */
                .log("➡️ Forwarding to processing route file=${header.CamelFileName}")
                .toD("seda:${header.MESSAGE_TYPE.toLowerCase()}-processing")

                /* =========================
                   COMPLETE
                   ========================= */
                .log("➡️ Routing airline=${header.AIRLINE_CODE} type=${header.MESSAGE_TYPE}");
    }

    /* ======================================================
       ABSTRACT METHODS
       ====================================================== */

    protected abstract String buildUri(ScheduleIngestionChannelEntity channel);

    protected abstract void validate(ScheduleIngestionChannelEntity channel);

    /* ======================================================
       HOOKS
       ====================================================== */

    protected void beforeProcessing(Exchange exchange) throws Exception {
        // optional override
    }

    /* ======================================================
       INTERNAL HELPERS
       ====================================================== */

    private void initializeContext(Exchange exchange) {
        exchange.setProperty("LOAD_ID", UUID.randomUUID().toString());
    }

    protected String buildRouteId(ScheduleIngestionChannelEntity c) {
        return String.format("INGEST-%s-%s-%s",
                c.getProfile().getAirlineCode(),
                c.getSourceType(),
                c.getMessageType());
    }

    protected <T> T val(T value, T def) {
        return value != null ? value : def;
    }
}