package com.codeshare.airline.platform.security.autoconfigure;

import com.codeshare.airline.platform.security.config.IdentityServiceSecurityProperties;
import com.codeshare.airline.platform.security.feign.IdentityServiceTokenClient;
import com.codeshare.airline.platform.security.feign.InternalAccessTokenProvider;
import com.codeshare.airline.platform.security.feign.RemoteInternalAccessTokenProvider;
import feign.RequestInterceptor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableFeignClients(clients = IdentityServiceTokenClient.class)
@EnableConfigurationProperties(IdentityServiceSecurityProperties.class)
public class InternalServiceFeignAutoConfiguration {

    private static final String[] INTERNAL_AUTH_OPTIONAL_PATH_PREFIXES = {
            "/internal/bootstrap/",
            "/internal/tenants",
            "/internal/airline-carriers/",
            "/tenant-ingestion-profiles/internal/"
    };

    @Bean
    @ConditionalOnMissingBean(InternalAccessTokenProvider.class)
    public InternalAccessTokenProvider internalAccessTokenProvider(
            IdentityServiceTokenClient tokenClient,
            IdentityServiceSecurityProperties properties
    ) {
        return new RemoteInternalAccessTokenProvider(tokenClient, properties);
    }

    @Bean
    public RequestInterceptor internalServiceBearerTokenInterceptor(
            ObjectProvider<InternalAccessTokenProvider> tokenProvider
    ) {
        return template -> {
            InternalAccessTokenProvider provider = tokenProvider.getIfAvailable();
            if (provider == null || template.headers().containsKey("Authorization") || isInternalAuthOptional(template.path())) {
                return;
            }

            template.header("Authorization", "Bearer " + provider.getAccessToken());
        };
    }

    private boolean isInternalAuthOptional(String path) {
        if (path == null || path.isBlank()) {
            return false;
        }

        for (String prefix : INTERNAL_AUTH_OPTIONAL_PATH_PREFIXES) {
            if (path.startsWith(prefix)) {
                return true;
            }
        }

        return false;
    }
}
