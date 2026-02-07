package com.codeshare.airline.auth.authentication.config;

import com.codeshare.airline.auth.authentication.security.filter.JwtAuthenticationFilter;
import com.codeshare.airline.auth.authentication.security.filter.TenantHeaderFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final TenantHeaderFilter tenantHeaderFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        log.info("Initializing WebSecurityConfig");

        http
                // ----------------------------------
                // Core security
                // ----------------------------------
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(List.of("http://localhost:4200"));
                    config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
                    config.setAllowedHeaders(List.of(
                            "Authorization",
                            "Content-Type",
                            "Accept",
                            "ssim-code"
                    ));
                    config.setAllowCredentials(true);
                    return config;
                }))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> {
                    log.debug("Configuring stateless session management");
                    sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })

                // ----------------------------------
                // Authorization rules
                // ----------------------------------
                .authorizeHttpRequests(auth -> {log.debug("Configuring authorization rules");

                    auth.requestMatchers(
                            "/api/auth/login",
                            "/api/auth/refresh",
                            "/api/auth/logout",
                            "/.well-known/**"
                    ).permitAll();

                    log.info("Public endpoints: /api/auth/login, /api/auth/refresh, /api/auth/logout, /.well-known/**");

                    auth.requestMatchers("/actuator/health", "/actuator/info").permitAll();

                    log.info("Public actuator endpoints: /actuator/health, /actuator/info");

                    auth.anyRequest().authenticated();
                    log.warn("All other endpoints are denied by default");
                })

                // ----------------------------------
                // Custom filters (ORDER MATTERS)
                // ----------------------------------
                .addFilterBefore(tenantHeaderFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class);


        log.info("Security filters registered: TenantHeaderFilter BEFORE UsernamePasswordAuthenticationFilter, " +"JwtAuthenticationFilter AFTER UsernamePasswordAuthenticationFilter");

        // ----------------------------------
        // Exception handling
        // ----------------------------------
        http.exceptionHandling(ex -> ex
                .authenticationEntryPoint((req, res, e) -> {
                    log.warn("Unauthorized access | path={} method={}",req.getRequestURI(),req.getMethod());
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                })
                .accessDeniedHandler((req, res, e) -> {
                    log.warn("Access denied | path={} method={}",req.getRequestURI(),req.getMethod());
                    res.sendError(HttpServletResponse.SC_FORBIDDEN);
                })
        );

        log.info("Spring Security filter chain successfully built");

        return http.build();
    }
}
