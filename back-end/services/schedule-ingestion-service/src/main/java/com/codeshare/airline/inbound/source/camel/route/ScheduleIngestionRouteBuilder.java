package com.codeshare.airline.inbound.source.camel.route;

import com.codeshare.airline.inbound.entities.source.ScheduleIngestionChannelEntity;
import com.codeshare.airline.inbound.entities.source.ScheduleIngestionProfileEntity;
import com.codeshare.airline.inbound.repositories.source.ScheduleIngestionProfileRepository;
import com.codeshare.airline.inbound.source.camel.channel.ChannelRouteBuilder;
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

        log.info("Initializing dynamic ingestion routes...");

        List<ScheduleIngestionProfileEntity> profiles = profileRepository.findAllWithChannels();

        profiles.stream()
                .filter(p -> Boolean.TRUE.equals(p.getEnabled()))
                .forEach(this::buildRoutesForProfile);

        try {
            camelContext.getRouteController().startAllRoutes();
            log.info("All routes started successfully");
        } catch (Exception e) {
            log.error("Failed to start routes", e);
        }

        initialized = true;
    }

    private void buildRoutesForProfile(ScheduleIngestionProfileEntity profile) {

        profile.getChannels().stream()
                .filter(channel -> isChannelEnabled(profile, channel))
                .forEach(channel -> channelBuilders.stream()
                        .filter(b -> b.supports() == channel.getSourceType())
                        .findFirst()
                        .ifPresentOrElse(
                                builder -> buildRoute(channel, builder),
                                () -> log.warn("No builder for sourceType={}", channel.getSourceType())
                        ));
    }

    private boolean isChannelEnabled(ScheduleIngestionProfileEntity profile, ScheduleIngestionChannelEntity channel) {
        boolean enabled = Boolean.TRUE.equals(channel.getEnabled());
        if (!enabled) {
            log.info(
                    "Skipping disabled ingestion channel airline={} sourceType={} messageType={}",
                    profile.getAirlineCode(),
                    channel.getSourceType(),
                    channel.getMessageType()
            );
        }
        return enabled;
    }

    private void buildRoute(ScheduleIngestionChannelEntity channel, ChannelRouteBuilder builder) {

        String routeId = String.format("INGEST-%s-%s-%s",
                channel.getProfile().getAirlineCode(),
                channel.getSourceType(),
                channel.getMessageType());

        if (camelContext.getRoute(routeId) != null) {
            log.warn("Route already exists: {}", routeId);
            return;
        }

        try {
            log.info("Creating route: {}", routeId);

            camelContext.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    builder.build(this, channel);
                }
            });

            camelContext.getRouteController().startRoute(routeId);
            log.info("Route started: {}", routeId);

        } catch (Exception e) {
            log.error("Failed to create route: {}", routeId, e);
        }
    }
}
