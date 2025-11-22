package com.codeshare.airline.gateway.controller;

import com.codeshare.airline.gateway.config.SwaggerServicesConfig;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api-docs")
public class SwaggerProxyController {

    private final SwaggerServicesConfig swaggerConfig;
    private final RouteLocator routeLocator;
    private final RestTemplate restTemplate;

    public SwaggerProxyController(SwaggerServicesConfig swaggerConfig, RouteLocator routeLocator, RestTemplate restTemplate) {
        this.swaggerConfig = swaggerConfig;
        this.routeLocator = routeLocator;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/{service}")
    public Mono<ResponseEntity<String>> proxy(@PathVariable String service) {

        var configOpt = swaggerConfig.getServices().stream()
                .filter(s -> s.getPath().equalsIgnoreCase(service))
                .findFirst();

        if (configOpt.isEmpty()) {
            return Mono.just(ResponseEntity.notFound().build());
        }

        var config = configOpt.get();

        // Find the actual route by serviceId and build API-docs URL dynamically
        return routeLocator.getRoutes()
                .filter(r -> r.getId().equals(config.getServiceId()))
                .next()
                .map(route -> {

                    String targetUri = route.getUri().toString();
                    if (targetUri.endsWith("/")) targetUri = targetUri.substring(0, targetUri.length() - 1);

                    String fullUrl = targetUri + config.getDocsPath();

                    try {
                        String json = restTemplate.getForObject(fullUrl, String.class);
                        return ResponseEntity.ok(json);
                    } catch (Exception e) {
                        return ResponseEntity.status(503)
                                .body("{\"error\":\"Unable to reach: " + fullUrl + "\"}");
                    }
                });
    }
}

