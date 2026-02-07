package com.codeshare.airline.security.client.config;

import com.codeshare.airline.security.client.properties.CSMOAuth2ClientProperties;
import feign.RequestInterceptor;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(CSMOAuth2ClientProperties.class)
@RequiredArgsConstructor
public class CSMOAuth2FeignClientConfig {

    private final CSMOAuth2ClientProperties properties;
    private final ClientRegistrationRepository clientRegistrationRepository;

    @PostConstruct
    void validateConfiguration() {

        ClientRegistration registration = clientRegistrationRepository.findByRegistrationId(properties.getRegistrationId());

        if (registration == null) {
            throw new IllegalStateException("❌ OAuth2 client registration not found: " + properties.getRegistrationId());
        }
        log.info("✅ OAuth2 Feign S2S enabled (registrationId={}, clientId={}, principal={})", properties.getRegistrationId(),  registration.getClientId(),
                properties.getPrincipal());
    }

    @Bean
    @ConditionalOnBean(OAuth2AuthorizedClientManager.class)
    public RequestInterceptor oauth2ClientCredentialsFeignInterceptor(OAuth2AuthorizedClientManager clientManager) {
        log.info("✅ OAuth2 Feign interceptor bean created");
        return template -> {

            // 0️⃣ Do not override Authorization if already set
            if (template.headers().containsKey("Authorization")) {
                return;
            }

            // 1️⃣ TOKEN RELAY (user → service)
            RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
            if (attrs instanceof ServletRequestAttributes servletAttrs) {
                HttpServletRequest request = servletAttrs.getRequest();
                String authHeader = request.getHeader("Authorization");

                if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
                    template.header("Authorization", authHeader);
                    return;
                }
            }

            // 2️⃣ SERVICE-TO-SERVICE (client_credentials)
            OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                            .withClientRegistrationId(properties.getRegistrationId())
                            .principal(properties.getPrincipal())
                            .build();

            // OAuth2AuthorizedClientManager handles token caching & refresh internally
            OAuth2AuthorizedClient client = clientManager.authorize(authorizeRequest);

            if (client == null || client.getAccessToken() == null) {
                throw new IllegalStateException("❌ Failed to obtain OAuth2 access token (registrationId="+ properties.getRegistrationId() + ")");
            }

            template.header("Authorization","Bearer " + client.getAccessToken().getTokenValue());
        };

    }

    public static void main(String[] args) {

        System.out.println(new BCryptPasswordEncoder().encode("admin123"));


    }
}
