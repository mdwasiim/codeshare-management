package com.codeshare.airline.schedule.ingestion.source.camel.route;

import com.codeshare.airline.schedule.ingestion.dto.source.AirlineIngestionChannelDTO;
import com.codeshare.airline.schedule.ingestion.dto.source.AirlineIngestionProfileDTO;
import com.codeshare.airline.schedule.ingestion.integration.tenant.TenantIngestionProfileClient;
import com.codeshare.airline.schedule.ingestion.source.camel.channel.ChannelRouteBuilder;
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
    private final TenantIngestionProfileClient profileClient;
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

        List<AirlineIngestionProfileDTO> profiles;
        try {
            profiles = profileClient.getAllProfiles();
        } catch (Exception ex) {
            log.error("Failed to load ingestion profiles from tenant-service", ex);
            return;
        }

        // Diagnostic logging: show how many profiles and a summary of each (helps debug file consumer configuration)
        log.info("Fetched {} ingestion profiles from tenant-service", profiles != null ? profiles.size() : 0);
        if (profiles != null) {
            profiles.forEach(p -> {
                int channelCount = p.getChannels() == null ? 0 : p.getChannels().size();
                log.info("Profile loaded -> airline={} enabled={} channels={}", p.getAirlineCode(), p.getEnabled(), channelCount);
                if (p.getChannels() != null) {
                    p.getChannels().forEach(ch -> log.debug(" Channel -> airline={} sourceType={} messageType={} remoteDir='{}' include='{}' preMove='{}' move='{}' moveFailed='{}' readLock='{}'",
                            p.getAirlineCode(),
                            ch.getSourceType(),
                            ch.getMessageType(),
                            ch.getRemoteDirectory(),
                            ch.getFileIncludePattern(),
                            ch.getFilePreMove(),
                            ch.getFileMove(),
                            ch.getFileMoveFailed(),
                            ch.getFileReadLock()));
                }
            });
        }

        profiles.stream()
                .filter(p -> Boolean.TRUE.equals(p.getEnabled()))
                .forEach(this::buildRoutesForProfile);

        initialized = true;
        log.info("Dynamic ingestion route initialization completed");
    }

    private void buildRoutesForProfile(AirlineIngestionProfileDTO profile) {

        profile.getChannels().stream()
                .filter(channel -> isChannelEnabled(profile, channel))
                .forEach(channel -> channelBuilders.stream()
                        .filter(b -> b.supports() == channel.getSourceType())
                        .findFirst()
                        .ifPresentOrElse(
                                builder -> buildRoute(profile, channel, builder),
                                () -> log.warn("No builder for sourceType={}", channel.getSourceType())
                        ));
    }

    private boolean isChannelEnabled(AirlineIngestionProfileDTO profile, AirlineIngestionChannelDTO channel) {
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

    private void buildRoute(AirlineIngestionProfileDTO profile, AirlineIngestionChannelDTO channel, ChannelRouteBuilder builder) {

        String routeId = String.format("INGEST-%s-%s-%s",
                profile.getAirlineCode(),
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
                    builder.build(this, profile, channel);
                }
            });

            camelContext.getRouteController().startRoute(routeId);
            log.info("Route started: {}", routeId);

        } catch (Exception e) {
            log.error("Failed to create route: {} using builder={} | cause={}",
                    routeId,
                    builder.getClass().getSimpleName(),
                    e.getMessage(),
                    e);
        }
    }
}
