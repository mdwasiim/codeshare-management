package com.codeshare.airline.gateway.config;

import com.codeshare.airline.gateway.exception.GatewayErrorWriter;
import com.codeshare.airline.gateway.properties.GatewayJwtSecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.OrServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Slf4j
@Configuration
@EnableWebFluxSecurity
@EnableConfigurationProperties(GatewayJwtSecurityProperties.class)
public class GatewaySecurityConfig {

    /* ======================================================
     *  AUTH SERVICE PUBLIC ENDPOINTS
     * ====================================================== */
    @Bean
    @Order(1)
    public SecurityWebFilterChain authPublicSecurity(
            ServerHttpSecurity http,
            GatewayJwtSecurityProperties props
    ) {

        List<ServerWebExchangeMatcher> matchers =
                props.getAuth().getPublicPaths().stream()
                        .map(PathPatternParserServerWebExchangeMatcher::new)
                        .map(ServerWebExchangeMatcher.class::cast)
                        .toList();

        ServerWebExchangeMatcher authMatcher =
                new OrServerWebExchangeMatcher(matchers);

        return http
                .securityMatcher(authMatcher)
                .cors(Customizer.withDefaults())
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(ex -> ex.anyExchange().permitAll())
                .build();
    }

    /* ======================================================
     *  API SECURITY (JWT PROTECTED)
     * ====================================================== */
    @Bean
    @Order(2)
    public SecurityWebFilterChain apiSecurity(
            ServerHttpSecurity http,
            GatewayJwtSecurityProperties props
    ) {

        return http
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> cors.configurationSource(corsConfiguration()))

                .authorizeExchange(ex -> {
                    ex.pathMatchers(
                            props.getApi().getPublicPaths().toArray(new String[0])
                    ).permitAll();
                    ex.anyExchange().authenticated();
                })

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(gatewayAuthEntryPoint())
                        .accessDeniedHandler(gatewayAccessDeniedHandler())
                )

                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                )

                .build();
    }

    /* ======================================================
     *  TENANT PROPAGATION FILTER
     * ====================================================== */
    @Bean
    @Order(-1)
    public GlobalFilter tenantPropagationFilter() {
        return (exchange, chain) ->
                exchange.getPrincipal()
                        .cast(JwtAuthenticationToken.class)
                        .flatMap(auth -> {

                            String tenant =
                                    auth.getToken().getClaimAsString("ssim");

                            var mutatedRequest =
                                    exchange.getRequest()
                                            .mutate()
                                            .header("X-Tenant-Id", tenant)
                                            .build();

                            return chain.filter(
                                    exchange.mutate()
                                            .request(mutatedRequest)
                                            .build()
                            );
                        })
                        .switchIfEmpty(chain.filter(exchange));
    }

    /* ======================================================
     *  JWT ROLE MAPPING
     * ====================================================== */
    @Bean
    public ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {

        ReactiveJwtAuthenticationConverter converter =
                new ReactiveJwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<String> roles = jwt.getClaimAsStringList("roles");

            if (roles == null) {
                return reactor.core.publisher.Flux.empty();
            }

            return reactor.core.publisher.Flux.fromIterable(roles)
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role));
        });

        return converter;
    }


    /* ======================================================
     *  CENTRALIZED ERROR HANDLING
     * ====================================================== */
    @Bean
    public ServerAuthenticationEntryPoint gatewayAuthEntryPoint() {
        return (exchange, ex) ->
                GatewayErrorWriter.write(
                        exchange.getResponse(),
                        HttpStatus.UNAUTHORIZED,
                        "invalid_token",
                        "Authentication required",
                        exchange
                );
    }

    @Bean
    public ServerAccessDeniedHandler gatewayAccessDeniedHandler() {
        return (exchange, ex) ->
                GatewayErrorWriter.write(
                        exchange.getResponse(),
                        HttpStatus.FORBIDDEN,
                        "access_denied",
                        "Insufficient permissions",
                        exchange
                );
    }

    /* ======================================================
     *  CORS CONFIGURATION
     * ====================================================== */
    private org.springframework.web.cors.reactive.CorsConfigurationSource corsConfiguration() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOriginPatterns(List.of(
                "https://*.codeshare.com",
                "http://localhost:*"
        ));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "X-Correlation-Id"
        ));
        config.setExposedHeaders(List.of("X-Correlation-Id"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        var source =
                new org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /* ======================================================
     *  SECURITY HEADERS
     * ====================================================== */
    @Bean
    public GlobalFilter securityHeadersFilter() {
        return (exchange, chain) -> {

            ServerHttpResponse res = exchange.getResponse();
            HttpHeaders h = res.getHeaders();

            h.add("X-Content-Type-Options", "nosniff");
            h.add("X-Frame-Options", "DENY");
            h.add("Referrer-Policy", "no-referrer");
            h.add("Permissions-Policy", "geolocation=(), camera=(), microphone=()");
            h.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
            h.add("Pragma", "no-cache");
            h.add(
                    "Content-Security-Policy",
                    "default-src 'self'; frame-ancestors 'none'; object-src 'none';"
            );
            h.add(
                    "Strict-Transport-Security",
                    "max-age=31536000; includeSubDomains"
            );

            return chain.filter(exchange);
        };
    }
}
