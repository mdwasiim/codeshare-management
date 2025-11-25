package com.codeshare.airline.gateway.controller;

import com.codeshare.airline.gateway.config.SwaggerServicesConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api-docs")
public class SwaggerProxyController {

    private final SwaggerServicesConfig swaggerConfig;
    private final RouteLocator routeLocator;
    private final WebClient webClient   ;

    @Autowired
    public SwaggerProxyController(SwaggerServicesConfig swaggerConfig, RouteLocator routeLocator, WebClient webClient) {
        this.swaggerConfig = swaggerConfig;
        this.routeLocator = routeLocator;
        this.webClient = webClient;
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
                .flatMap(route -> {

                    String targetUri = route.getUri().toString().replaceAll("/$", "");
                    String fullUrl = targetUri + config.getDocsPath();

                    return webClient.get()
                            .uri(fullUrl)
                            .accept(MediaType.APPLICATION_JSON)
                            .retrieve()
                            .bodyToMono(String.class)
                            .map(ResponseEntity::ok)
                            .onErrorReturn(ResponseEntity
                                    .status(503)
                                    .body("{\"error\": \"Unable to reach: " + fullUrl + "\"}")
                            );
                });
    }
}

