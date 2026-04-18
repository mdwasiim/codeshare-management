package com.codeshare.airline.inbound.source.service;

import com.codeshare.airline.inbound.source.camel.route.CoreIngestionRouteBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleRouteRefreshService {

    private final CamelContext camelContext;
    private final CoreIngestionRouteBuilder routeBuilder;

    public synchronized void refreshRoutes() throws Exception {

        log.info("🔄 Refreshing ingestion routes...");

        List<String> ingestionRoutes = camelContext.getRoutes().stream()
                .map(Route::getId)
                .filter(this::isIngestionRoute)
                .toList();

        stopAndRemoveRoutes(ingestionRoutes);

        log.info(" Rebuilding ingestion routes...");
        camelContext.addRoutes(routeBuilder);

        log.info(" Route refresh completed");
    }

    /* ========================= */

    private boolean isIngestionRoute(String id) {
        return id != null && id.startsWith("INGEST-");
    }

    private void stopAndRemoveRoutes(List<String> routeIds) throws Exception {

        for (String routeId : routeIds) {

            log.info("🛑 Stopping route {}", routeId);

            camelContext.getRouteController().stopRoute(routeId);
            camelContext.removeRoute(routeId);
        }
    }
}