package com.codeshare.airline.identity.authentication.config;

import com.codeshare.airline.identity.authentication.security.filter.TenantHeaderFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final TenantHeaderFilter tenantHeaderFilter;
    private final SecurityProperties securityProperties;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        log.info("Initializing WebSecurityConfig");

        http
                // ----------------------------------
                // Core security
                // ----------------------------------
                /*.cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(securityProperties.getCors().getAllowedOrigins());
                    config.setAllowedMethods(securityProperties.getCors().getAllowedMethods());
                    config.setAllowedHeaders(securityProperties.getCors().getAllowedHeaders());
                    config.setAllowCredentials(securityProperties.getCors().isAllowCredentials());
                    return config;
                }))*/
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
                            "/auth/login",
                            "/auth/refresh",
                            "/auth/logout",
                            "/.well-known/**"
                    ).permitAll();

                    log.info("Public endpoints: /auth/login, /auth/refresh, /auth/logout, /.well-known/**");

                    auth.requestMatchers("/actuator/health", "/actuator/info").permitAll();

                    log.info("Public actuator endpoints: /actuator/health, /actuator/info");

                    auth.anyRequest().authenticated();
                    log.warn("All other endpoints are denied by default");
                })

                // ----------------------------------
                // ✅ NEW: Spring JWT handling
                // ----------------------------------
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                )

                // ----------------------------------
                // Tenant filter only
                // ----------------------------------
                .addFilterBefore(tenantHeaderFilter, UsernamePasswordAuthenticationFilter.class)


                // ----------------------------------
                // Exception handling
                // ----------------------------------
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, e) -> {
                            log.warn("Unauthorized access | path={} method={}", req.getRequestURI(), req.getMethod());
                            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                        })
                        .accessDeniedHandler((req, res, e) -> {
                            log.warn("Access denied | path={} method={}", req.getRequestURI(), req.getMethod());
                            res.sendError(HttpServletResponse.SC_FORBIDDEN);
                        })
                );

        log.info("Spring Security filter chain successfully built");

        return http.build();
    }


    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<String> roles = jwt.getClaim("roles");

            if (roles == null) {
                return List.of();
            }

            return roles.stream()
                    .map(role -> (GrantedAuthority) new SimpleGrantedAuthority("ROLE_" + role))
                    .toList();
        });

        return converter;
    }
}
