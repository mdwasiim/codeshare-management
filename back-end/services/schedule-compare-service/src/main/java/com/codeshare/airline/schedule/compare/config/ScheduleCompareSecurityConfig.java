package com.codeshare.airline.schedule.compare.config;

import com.codeshare.airline.platform.security.web.InternalEndpointAuthorization;
import com.codeshare.airline.platform.security.web.StatelessResourceServerSecuritySupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ScheduleCompareSecurityConfig {

    @Bean
    public SecurityFilterChain scheduleCompareSecurityFilterChain(HttpSecurity http) throws Exception {
        StatelessResourceServerSecuritySupport.apply(http)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                        .requestMatchers(
                                "/schedule/internal/change-sets/**",
                                "/internal/change-sets/**"
                        ).hasAuthority(InternalEndpointAuthorization.INTERNAL_SCOPE_AUTHORITY)
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
