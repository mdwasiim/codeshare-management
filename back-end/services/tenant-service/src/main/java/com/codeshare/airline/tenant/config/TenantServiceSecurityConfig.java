package com.codeshare.airline.tenant.config;

import com.codeshare.airline.platform.security.web.StatelessResourceServerSecuritySupport;
import com.codeshare.airline.platform.security.web.InternalEndpointAuthorization;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class TenantServiceSecurityConfig {

    @Bean
    public SecurityFilterChain tenantServiceSecurityFilterChain(HttpSecurity http) throws Exception {
        StatelessResourceServerSecuritySupport.apply(http)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/tenants/login-options",
                                "/tenant/tenants/login-options",
                                "/tenants/code/*/auth-context",
                                "/tenant/tenants/code/*/auth-context",
                                "/actuator/health",
                                "/actuator/info"
                        ).permitAll()
                        .requestMatchers(
                                "/internal/tenants",
                                "/internal/tenants/**",
                                "/tenant-ingestion-profiles/internal/**",
                                "/tenant-partner-acceptance-rules/internal/**",
                                "/tenant-partner-communication-profiles/internal/**",
                                "/tenant-partner-distribution-profiles/internal/**"
                        ).hasAuthority(InternalEndpointAuthorization.INTERNAL_SCOPE_AUTHORITY)
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
