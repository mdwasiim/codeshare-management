package com.codeshare.airline.security.client.feign;

import com.codeshare.airline.security.client.oauth2.CSMOAuth2ClientTokenService;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RequiredArgsConstructor
public class CSMOAuth2FeignRequestInterceptor implements RequestInterceptor {

    private final CSMOAuth2ClientTokenService tokenProvider;

    @Override
    public void apply(RequestTemplate template) {

        // 0️⃣ Do not override existing Authorization
        if (template.headers().containsKey("Authorization")) {
            return;
        }

        // 1️⃣ Forward user Bearer token if present
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs instanceof ServletRequestAttributes servletAttrs) {
            HttpServletRequest request = servletAttrs.getRequest();
            String authHeader = request.getHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                template.header("Authorization", authHeader);
                return;
            }
        }

        // 2️⃣ Fallback to cached service token
        template.header("Authorization","Bearer " + tokenProvider.getAccessToken()
        );
    }
}

