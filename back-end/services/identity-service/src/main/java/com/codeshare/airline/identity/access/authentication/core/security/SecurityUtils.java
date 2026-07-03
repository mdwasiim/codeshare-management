package com.codeshare.airline.identity.access.authentication.core.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    public String getCurrentUsername() {
        Authentication authentication = getAuthentication();

        return authentication.getName();
    }

    public Authentication getAuthentication() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        return authentication;
    }

    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        return authentication != null && authentication.isAuthenticated();
    }
}