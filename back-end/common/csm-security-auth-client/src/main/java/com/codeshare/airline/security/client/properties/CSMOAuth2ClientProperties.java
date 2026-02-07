package com.codeshare.airline.security.client.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "security.internal.oauth2")
public class CSMOAuth2ClientProperties {

    /**
     * Client registration id (must match spring.security.oauth2.gateway.registration.*)
     */
    private String registrationId;

    /**
     * Logical service principal name
     */
    private String principal;
}