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

        boolean skip = path.equals("/api/auth/login")
                || path.equals("/api/auth/refresh")
                || path.startsWith("/.well-known/");

        if (skip) {
            log.debug("Skipping JwtAuthenticationFilter for path: {}", path);
        }

        return skip;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();
        log.debug("JwtAuthenticationFilter invoked for path: {}", path);

        // -----------------------------------------
        // 1️⃣ LOGIN / REFRESH → Tenant from HEADER
        // -----------------------------------------
        if (path.equals("/api/auth/login") || path.equals("/api/auth/refresh")) {

            String tenantCode = request.getHeader("tenant-code");

            if (tenantCode == null || tenantCode.isBlank()) {
                log.warn(
                        "Missing tenant-code header | method={} path={} remoteAddr={}",
                        request.getMethod(),
                        path,
                        request.getRemoteAddr()
                );
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing tenant-code header");
                return;
            }

            log.debug("Validating tenant from header for login/refresh: {}", tenantCode);
            tenantContextResolver.validateTenant(tenantCode);

            filterChain.doFilter(request, response);
            return;
        }

        // -----------------------------------------
        // 2️⃣ OTHER REQUESTS → JWT REQUIRED
        // -----------------------------------------
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug(
                    "No Bearer token found | method={} path={}",
                    request.getMethod(),
                    path
            );
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            log.debug("Validating JWT token for path: {}", path);

            JwtAuthenticationClaims claims = jwtValidator.validate(token);

            if (claims.getRoles() == null || claims.getRoles().isEmpty()) {
                log.warn("JWT token has no roles | userId={}", claims.getUserId());
                throw new TokenValidationException("Token has no roles");
            }

            // ✅ Tenant validation from token
            log.debug(
                    "Validating tenant from token | tenantCode={} userId={}",
                    claims.getTenantCode(),
                    claims.getUserId()
            );
            tenantContextResolver.validateTenant(claims.getTenantCode());

            Authentication authentication = buildAuthentication(claims);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.info(
                    "JWT authentication successful | userId={} username={} tenantCode={}",
                    claims.getUserId(),
                    claims.getUsername(),
                    claims.getTenantCode()
            );

        } catch (TokenValidationException ex) {
            SecurityContextHolder.clearContext();
            log.warn(
                    "JWT validation failed | path={} reason={}",
                    path,
                    ex.getMessage()
            );
        } catch (Exception ex) {
            SecurityContextHolder.clearContext();
            log.error(
                    "Unexpected error during JWT authentication | path={}",
                    path,
                    ex
            );
        }

        filterChain.doFilter(request, response);
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
