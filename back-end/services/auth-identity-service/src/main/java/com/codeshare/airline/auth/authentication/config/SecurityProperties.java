package com.codeshare.airline.auth.authentication.config;

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
}

