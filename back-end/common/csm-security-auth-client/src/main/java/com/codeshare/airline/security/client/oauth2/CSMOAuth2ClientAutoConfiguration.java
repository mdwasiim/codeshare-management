package com.codeshare.airline.security.client.oauth2;

import com.codeshare.airline.security.client.feign.CSMOAuth2FeignRequestInterceptor;
import com.codeshare.airline.security.client.properties.CSMOAuth2ClientProperties;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@Slf4j
@AutoConfiguration
@ConditionalOnClass(CSMOAuth2ClientTokenService.class)
@EnableConfigurationProperties(CSMOAuth2ClientProperties.class)
public class CSMOAuth2ClientAutoConfiguration {

    public CSMOAuth2ClientAutoConfiguration() {
        log.debug("Initializing CSMOAuth2ClientAutoConfiguration");
    }

    @Bean
    public RequestInterceptor oauth2FeignInterceptor(
            CSMOAuth2ClientTokenService tokenProvider) {

        log.info("Registering OAuth2 Feign RequestInterceptor (CSMOAuth2FeignRequestInterceptor)");

        return new CSMOAuth2FeignRequestInterceptor(tokenProvider);
    }

    @Bean
    @ConditionalOnMissingBean(OAuth2AuthorizedClientManager.class)
    public OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository registrations,
            OAuth2AuthorizedClientService clientService) {

        log.info("Creating OAuth2AuthorizedClientManager (client_credentials + refresh_token)");

        OAuth2AuthorizedClientProvider provider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .clientCredentials()
                        .refreshToken()
                        .build();

        AuthorizedClientServiceOAuth2AuthorizedClientManager manager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                        registrations, clientService);

        manager.setAuthorizedClientProvider(provider);

        log.debug("OAuth2AuthorizedClientManager configured successfully");

        return manager;
    }
}
