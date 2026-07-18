package com.codeshare.airline.master.config;

import com.codeshare.airline.platform.security.web.StatelessResourceServerSecuritySupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class MasterDataSecurityConfig {

    @Bean
    public SecurityFilterChain masterDataSecurityFilterChain(HttpSecurity http) throws Exception {
        StatelessResourceServerSecuritySupport.apply(http)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/internal/airline-carriers/**",
                                "/actuator/health",
                                "/actuator/info"
                        ).permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
