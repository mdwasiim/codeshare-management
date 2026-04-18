package com.codeshare.airline.tenant.common;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Configuration(proxyBeanMethods = false)
public class TenantFeignWebContextConfiguration {

    @Bean
    public RequestInterceptor webContextForwardingInterceptor() {
        return template -> {

            // 🔐 Do NOT override OAuth2 Authorization
            if (template.headers().containsKey("Authorization")) {
                log.trace(
                        "Authorization already present for Feign call [{} {}] – skipping web forwarding",
                        template.method(),
                        template.url()
                );
                return;
            }

            RequestAttributes ra = RequestContextHolder.getRequestAttributes();
            if (!(ra instanceof ServletRequestAttributes attrs)) {
                log.trace(
                        "No web request context for Feign call [{} {}]",
                        template.method(),
                        template.url()
                );
                return;
            }

            HttpServletRequest request = attrs.getRequest();

            // -----------------------
            // Authorization (USER TOKEN ONLY)
            // -----------------------
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null) {
                template.header("Authorization", authHeader);
                log.debug(
                        "Forwarded user Authorization for Feign call [{} {}]",
                        template.method(),
                        template.url()
                );
            }

            // -----------------------
            // TenantUser-Agent
            // -----------------------
            String userAgent = request.getHeader("TenantUser-Agent");
            if (userAgent != null) {
                template.header("TenantUser-Agent", userAgent);
            }

            // -----------------------
            // Client IP
            // -----------------------
            String ip = request.getHeader("X-Forwarded-For");
            if (ip == null) ip = request.getHeader("X-Real-IP");
            if (ip == null) ip = request.getRemoteAddr();

            template.header("X-Client-IP", ip);
        };
    }

}
