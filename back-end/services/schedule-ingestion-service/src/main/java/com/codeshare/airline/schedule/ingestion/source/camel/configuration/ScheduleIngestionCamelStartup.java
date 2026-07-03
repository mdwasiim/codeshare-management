package com.codeshare.airline.schedule.ingestion.source.camel.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleIngestionCamelStartup {

    private final CamelContext camelContext;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {

        log.info(" Application is ready");

        log.info("Camel Context Name: {}", camelContext.getName());
        log.info("Camel Version: {}", camelContext.getVersion());

        log.info("📊 Total routes loaded: {}", camelContext.getRoutes().size());

        camelContext.getRoutes().forEach(route ->
                log.info("➡️ Route [{}] status={}",
                        route.getId(),
                        camelContext.getRouteController().getRouteStatus(route.getId()))
        );

        log.info(" Camel is fully initialized and running");
    }
}