package com.codeshare.airline.auth.authentication.security.filter;

import com.codeshare.airline.auth.authentication.exception.TokenValidationException;
import com.codeshare.airline.auth.authentication.security.adapter.UserDetailsAdapter;
import com.codeshare.airline.auth.authentication.security.jwt.JwtAuthenticationClaims;
import com.codeshare.airline.auth.authentication.security.jwt.JwtValidator;
import com.codeshare.airline.auth.authentication.service.core.TenantContextResolver;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtValidator jwtValidator;
    private final TenantContextResolver tenantContextResolver;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        boolean skip = path.startsWith("/api/auth/") || path.startsWith("/.well-known/");

        if (skip) {
            log.debug("Skipping JwtAuthenticationFilter for path: {}", path);
        }

        return skip;
    }

    @Override
    protected void doFilterInternal( HttpServletRequest request, HttpServletResponse response, FilterChain filterChain ) throws ServletException, IOException {

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing Authorization header");
            return;
        }

        try {
            String token = authHeader.substring(7);
            JwtAuthenticationClaims claims = jwtValidator.validate(token);

            tenantContextResolver.validateTenant(claims.getTenantCode());

            Authentication authentication = buildAuthentication(claims);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (TokenValidationException ex) {
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
        }
    }

    private Authentication buildAuthentication(JwtAuthenticationClaims claims) {

        List<GrantedAuthority> authorities = claims.getRoles()
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toUnmodifiableList());

        UserDetails principal = new UserDetailsAdapter(
                claims.getUserId(),
                claims.getUsername(),
                claims.getTenantId(),
                claims.getTenantCode(),
                true,
                true,
                authorities
        );

        return new UsernamePasswordAuthenticationToken(principal, null, authorities);
    }
}
