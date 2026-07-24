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
            if (provider == null || template.headers().containsKey("Authorization")) {
                return;
            }

            template.header("Authorization", "Bearer " + provider.getAccessToken());
        };
    }
}
