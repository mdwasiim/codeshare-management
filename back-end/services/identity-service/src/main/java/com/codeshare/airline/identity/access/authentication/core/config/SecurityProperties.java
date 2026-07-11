package com.codeshare.airline.identity.access.authentication.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@ConfigurationProperties(prefix = "security")
@Getter
@Setter
public class SecurityProperties {

    private Jwt jwt;
    private Oidc oidc;
    private Cors cors;
    private PublicEndpoints publicEndpoints;
    private S2s s2s;

    @Getter @Setter
    public static class Jwt {
        private String issuer;
        private String audience;
        private long accessTokenTtl;
        private long refreshTokenTtl;
        private Keystore keystore;
    }

    @Getter @Setter
    public static class Keystore {
        private Resource location;
        private String storePassword;
        private String keyAlias;
        private String keyPassword;
    }

    @Getter @Setter
    public static class Oidc {
        private State state;

        @Getter @Setter
        public static class State {
            private String secret;
            private long ttlSeconds;
        }
    }

    @Getter @Setter
    public static class Cors {
        private java.util.List<String> allowedOrigins = java.util.List.of("http://localhost:4200");
        private java.util.List<String> allowedMethods = java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS");
        private java.util.List<String> allowedHeaders = java.util.List.of(
                "Authorization",
                "Content-Type",
                "Accept",
                "X-Tenant-Id"
        );
        private boolean allowCredentials = true;
    }

    @Getter @Setter
    public static class PublicEndpoints {
        private java.util.List<String> auth = java.util.List.of(
                "/auth/login",
                "/auth/refresh",
                "/auth/logout",
                "/auth/service-token",
                "/.well-known/**"
        );
        private java.util.List<String> actuator = java.util.List.of(
                "/actuator/health",
                "/actuator/info"
        );
    }

    @Getter @Setter
    public static class S2s {
        private String clientId;
        private String clientSecret;
    }
}
