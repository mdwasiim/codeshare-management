package com.codeshare.airline.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "services.identity")
public class IdentityServiceSecurityProperties {

    private String url = "http://localhost:8081";
    private final S2s s2s = new S2s();

    @Getter
    @Setter
    public static class S2s {
        private String clientId = "internal-s2s-client";
        private String clientSecret = "admin123";
    }
}
