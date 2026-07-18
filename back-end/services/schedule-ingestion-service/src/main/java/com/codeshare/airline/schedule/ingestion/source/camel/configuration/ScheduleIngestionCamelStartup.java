package com.codeshare.airline.schedule.ingestion.source.camel.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.ServiceStatus;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleIngestionCamelStartup {

    private final CamelContext camelContext;

    @EventListener(ApplicationReadyEvent.class)
    @Order(1)
    public void onApplicationReady() {

        log.info(" Application is ready");

        log.info("Camel Context Name: {}", camelContext.getName());
        log.info("Camel Version: {}", camelContext.getVersion());

        try {
            if (camelContext.getStatus() != ServiceStatus.Started) {
                log.info("Starting Camel context manually because auto-startup is disabled");
                camelContext.start();
            }
            startLoadedRoutes();
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to start Camel context", ex);
        }

        log.info("📊 Total routes loaded: {}", camelContext.getRoutes().size());

        camelContext.getRoutes().forEach(route ->
                log.info("➡️ Route [{}] status={}",
                        route.getId(),
                        camelContext.getRouteController().getRouteStatus(route.getId()))
        );

        log.info(" Camel is fully initialized and running");
    }

    private void startLoadedRoutes() throws Exception {
        for (org.apache.camel.Route route : camelContext.getRoutes()) {
            String routeId = route.getId();
            ServiceStatus routeStatus = camelContext.getRouteController().getRouteStatus(routeId);
            if (routeStatus != ServiceStatus.Started) {
                log.info("Starting Camel route [{}] because auto-startup is disabled", routeId);
                camelContext.getRouteController().startRoute(routeId);
            }
        }
    }
}
