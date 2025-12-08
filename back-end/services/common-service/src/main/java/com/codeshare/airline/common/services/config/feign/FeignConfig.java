package com.codeshare.airline.common.services.config.feign;


import feign.Logger;
import feign.Request;
import feign.RequestInterceptor;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.TimeUnit;

/**
 * Global Feign configuration for Tenant Service.
 *
 * This class configures:
 *   ✔ Automatic forwarding of authentication headers (JWT)
 *   ✔ User metadata forwarding (IP, User-Agent)
 *   ✔ Feign timeouts
 *   ✔ Retry mechanism for transient failures
 *   ✔ Logging level
 *   ✔ Custom error decoder for standardized error handling
 *
 * These configurations apply only to Feign clients that explicitly reference this config.
 */
@Configuration
@RequiredArgsConstructor
public class FeignConfig {

    /**
     * RequestInterceptor is executed BEFORE every Feign HTTP call.
     *
     * Purpose:
     *   ✔ Forward Authorization header → pass JWT token to downstream services
     *   ✔ Forward User-Agent → maintain client traceability
     *   ✔ Forward client IP → helpful for logging, audit, security checks
     *   ✔ Support for gateway-provided X-Forwarded-For and X-Real-IP
     *
     * Note:
     *   - RequestContextHolder works only inside an HTTP request thread.
     *   - If Feign is called from async or @Scheduled jobs, headers will NOT be forwarded (expected).
     */
    @Bean
    public RequestInterceptor authForwardInterceptor() {
        return template -> {

            // Retrieve the current HTTP request bound to this thread
            RequestAttributes ra = RequestContextHolder.getRequestAttributes();
            if (!(ra instanceof ServletRequestAttributes attrs)) {
                // Feign call occurred outside of an HTTP request (async or scheduled)
                return;
            }

            HttpServletRequest request = attrs.getRequest();

            /** -----------------------------
             * 1. Forward Authorization header
             * ----------------------------- */
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null) {
                template.header("Authorization", authHeader);
            }

            /** -----------------------------
             * 2. Forward User-Agent header
             * ----------------------------- */
            String userAgent = request.getHeader("User-Agent");
            if (userAgent != null) {
                template.header("User-Agent", userAgent);
            }

            /** -----------------------------
             * 3. Forward Client IP Address
             * ----------------------------- */
            String ip = request.getHeader("X-Forwarded-For");  // Load balancer / gateway
            if (ip == null) ip = request.getHeader("X-Real-IP");
            if (ip == null) ip = request.getRemoteAddr();      // Fallback to container IP

            template.header("X-Client-IP", ip);
        };
    }

    /**
     * Configure Feign client-level timeouts.
     *
     * connectionTimeout: time allowed to establish a socket connection
     * readTimeout: time allowed for server to respond after connection
     *
     * Best-practice:
     *   - Never allow infinite hanging
     *   - Keep Feign timeouts LOWER than CircuitBreaker timeout
     */
    @Bean
    public Request.Options requestOptions() {
        return new Request.Options(
                5000, TimeUnit.MILLISECONDS,   // 5s connection timeout
                10000, TimeUnit.MILLISECONDS,  // 10s read timeout
                true                           // Follow redirects
        );
    }

    /**
     * Feign Retry policy.
     *
     * Purpose:
     *   Retry failed calls caused by transient issues such as:
     *     ✔ Temporary network glitch
     *     ✔ Slow responding downstream service
     *
     * Parameters:
     *   initialInterval = 200ms
     *   maxInterval = 2000ms
     *   maxAttempts = 3
     *
     * Note:
     *   - Do NOT retry authentication failures (401)
     *   - Only retries FEIGN I/O-related failures
     */
    @Bean
    public Retryer retryer() {
        return new Retryer.Default(
                200,     // initial retry interval (ms)
                2000,    // maximum retry interval (ms)
                3        // total retry attempts
        );
    }

    /**
     * Feign logging level.
     *
     * Levels:
     *   NONE  – No logs (best for production)
     *   BASIC – Logs request method + URL + response status
     *   HEADERS – Logs headers only
     *   FULL – Logs everything including body (best for debugging)
     *
     * Recommendation:
     *   - FULL for development
     *   - BASIC for production (to avoid log bloat)
     */
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    /**
     * Custom Error Decoder for Feign client.
     *
     * Purpose:
     *   - Convert Feign HTTP errors into readable, structured exceptions
     *   - Ensure uniform error handling across all microservices
     *   - Allow central handling in GlobalExceptionHandler
     *
     * FeignErrorDecoder is implemented in common-service module.
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }
}
