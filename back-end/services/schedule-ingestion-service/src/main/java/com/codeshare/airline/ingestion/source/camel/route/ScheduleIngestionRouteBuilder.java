package com.codeshare.airline.ingestion.source.camel.route;

import com.codeshare.airline.ingestion.source.camel.channel.ChannelRouteBuilder;
import com.codeshare.airline.ingestion.persistence.entities.source.ScheduleIngestionChannelEntity;
import com.codeshare.airline.ingestion.persistence.entities.source.ScheduleIngestionProfileEntity;
import com.codeshare.airline.ingestion.persistence.repositories.source.ScheduleIngestionProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleIngestionRouteBuilder {

    private final CamelContext camelContext;
    private final ScheduleIngestionProfileRepository profileRepository;
    private final List<ChannelRouteBuilder> channelBuilders;

    private volatile boolean initialized = false;

    @EventListener(ApplicationReadyEvent.class)
    @Order(2)
    public synchronized void initRoutes() {

        if (initialized) {
            log.warn("Routes already initialized. Skipping...");
            return;
        }

        log.info(" Initializing dynamic ingestion routes...");

        List<ScheduleIngestionProfileEntity> profiles = profileRepository.findAllWithChannels();

        profiles.stream()
                .filter(p -> Boolean.TRUE.equals(p.getEnabled()))
                .forEach(this::buildRoutesForProfile);

        //  FIX: Start ALL routes (not per route)
        try {
            camelContext.getRouteController().startAllRoutes();
            log.info(" All routes started successfully");
        } catch (Exception e) {
            log.error(" Failed to start routes", e);
        }

        initialized = true;
    }

    /* ======================================================
       PROFILE PROCESSING
       ====================================================== */

    private void buildRoutesForProfile(ScheduleIngestionProfileEntity profile) {

        profile.getChannels().forEach(channel -> {

            channelBuilders.stream()
                    .filter(b -> b.supports() == channel.getSourceType())
                    .findFirst()
                    .ifPresentOrElse(
                            builder -> buildRoute(channel, builder),
                            () -> log.warn("⚠️ No builder for sourceType={}", channel.getSourceType())
                    );
        });
    }

    /* ======================================================
       ROUTE CREATION
       ====================================================== */

    private void buildRoute(ScheduleIngestionChannelEntity channel, ChannelRouteBuilder builder) {

        String routeId = String.format("INGEST-%s-%s-%s",
                channel.getProfile().getAirlineCode(),
                channel.getSourceType(),
                channel.getMessageType());

        if (camelContext.getRoute(routeId) != null) {
            log.warn("⚠️ Route already exists: {}", routeId);
            return;
        }

        try {
            log.info("🔥 Creating route: {}", routeId);

            camelContext.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    builder.build(this, channel);
                }
            });

            // 🔥 Start ALL routes to ensure SEDA consumer is active
            //camelContext.getRouteController().startAllRoutes();
            camelContext.getRouteController().startRoute(routeId);
            log.info(" All routes started");

        } catch (Exception e) {
            log.error(" Failed to create route: {}", routeId, e);
        }
    }
}