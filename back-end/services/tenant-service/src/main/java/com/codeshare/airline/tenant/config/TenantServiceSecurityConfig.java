package com.codeshare.airline.tenant.config;

import com.codeshare.airline.platform.security.web.StatelessResourceServerSecuritySupport;
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
                                "/tenants/code/*/auth-context",
                                "/internal/tenants",
                                "/internal/tenants/**",
                                "/tenant-ingestion-profiles/internal/**",
                                "/actuator/health",
                                "/actuator/info"
                        ).permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
