package com.codeshare.airline.auth.authentication.config;

import com.codeshare.airline.auth.authentication.security.filter.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // ----------------------------------
                // Core security
                // ----------------------------------
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // ----------------------------------
                // Authorization rules
                // ----------------------------------
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/refresh",
                                "/api/auth/logout",
                                "/.well-known/**"
                        ).permitAll()

                        // actuator: restrict later
                        .requestMatchers("/actuator/health", "/actuator/info")
                        .permitAll()

                        .anyRequest().denyAll()
                )

                // ----------------------------------
                // Custom login filter
                // ----------------------------------
                .addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class)

                // ----------------------------------
                // Exception handling (optional but recommended)
                // ----------------------------------
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(
                                (req, res, e) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED)
                        )
                        .accessDeniedHandler(
                                (req, res, e) -> res.sendError(HttpServletResponse.SC_FORBIDDEN)
                        )
                );

        return http.build();
    }
}
