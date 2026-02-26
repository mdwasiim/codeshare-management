package com.codeshare.airline.schedule.source.camel.lifecycle;

import com.codeshare.airline.schedule.source.camel.route.DynamicIngestionRouteBuilder;
import lombok.RequiredArgsConstructor;
import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteRefreshService {

    private final CamelContext camelContext;
    private final DynamicIngestionRouteBuilder dynamicRouteBuilder;

    public synchronized void refreshRoutes() throws Exception {

        // 1️⃣ Find ingestion routes only (exclude processing route)
        List<String> routeIds = camelContext.getRoutes()
                .stream()
                .map(Route::getId)
                .filter(id -> id != null && id.contains("-")) // our dynamic IDs
                .toList();

        // 2️⃣ Stop and remove them
        for (String routeId : routeIds) {
            camelContext.getRouteController().stopRoute(routeId);
            camelContext.removeRoute(routeId);
        }

        // 3️⃣ Re-add dynamic routes
        camelContext.addRoutes(dynamicRouteBuilder);
    }
}