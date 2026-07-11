package com.codeshare.airline.security.autoconfigure;

import com.codeshare.airline.security.config.IdentityServiceSecurityProperties;
import com.codeshare.airline.security.feign.InternalAccessTokenProvider;
import com.codeshare.airline.security.feign.IdentityServiceTokenClient;
import com.codeshare.airline.security.feign.RemoteInternalAccessTokenProvider;
import feign.RequestInterceptor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(IdentityServiceSecurityProperties.class)
public class InternalServiceFeignAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(InternalAccessTokenProvider.class)
    @ConditionalOnBean(IdentityServiceTokenClient.class)
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
            if (provider == null || template.headers().containsKey("Authorization")) {
                return;
            }

            template.header("Authorization", "Bearer " + provider.getAccessToken());
        };
    }
}
