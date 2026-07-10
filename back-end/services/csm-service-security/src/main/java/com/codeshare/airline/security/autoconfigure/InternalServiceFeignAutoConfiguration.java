package com.codeshare.airline.security.autoconfigure;

import com.codeshare.airline.security.config.IdentityServiceSecurityProperties;
import com.codeshare.airline.security.feign.InternalAccessTokenProvider;
import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(IdentityServiceSecurityProperties.class)
public class InternalServiceFeignAutoConfiguration {

    @Bean
    @ConditionalOnBean(InternalAccessTokenProvider.class)
    public RequestInterceptor internalServiceBearerTokenInterceptor(InternalAccessTokenProvider tokenProvider) {
        return template -> {
            if (!template.headers().containsKey("Authorization")) {
                template.header("Authorization", "Bearer " + tokenProvider.getAccessToken());
            }
        };
    }
}
