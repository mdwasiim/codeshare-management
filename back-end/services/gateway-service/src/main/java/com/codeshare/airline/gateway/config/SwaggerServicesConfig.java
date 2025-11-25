package com.codeshare.airline.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "swagger")
public class SwaggerServicesConfig {

    private List<ServiceConfig> services;

    @Data
    public static class ServiceConfig {
        private String name;
        private String path;        // /auth
        private String serviceId;   // auth-identity-service (gateway route id)
        private String docsPath;    // /v3/api-docs
    }

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder.build();
    }
}
