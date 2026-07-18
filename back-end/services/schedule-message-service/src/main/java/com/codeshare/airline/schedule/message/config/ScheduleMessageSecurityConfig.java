package com.codeshare.airline.schedule.message.config;

import com.codeshare.airline.platform.security.web.InternalEndpointAuthorization;
import com.codeshare.airline.platform.security.web.StatelessResourceServerSecuritySupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ScheduleMessageSecurityConfig {

    @Bean
    public SecurityFilterChain scheduleMessageSecurityFilterChain(HttpSecurity http) throws Exception {
        StatelessResourceServerSecuritySupport.apply(http)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                        .requestMatchers(
                                "/schedule/internal/outbound-messages/**",
                                "/internal/outbound-messages/**"
                        ).hasAuthority(InternalEndpointAuthorization.INTERNAL_SCOPE_AUTHORITY)
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
