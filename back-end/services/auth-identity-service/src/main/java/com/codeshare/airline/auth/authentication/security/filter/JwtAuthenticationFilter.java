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

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtValidator jwtValidator;
    private final TenantContextResolver tenantContextResolver;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/auth/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader(org.springframework.http.HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            JwtAuthenticationClaims claims = jwtValidator.validate(token);

            if (claims.getRoles() == null || claims.getRoles().isEmpty()) {
                throw new TokenValidationException("Token has no roles");
            }
            // ✅ Tenant validation
            tenantContextResolver.validateTenant(claims.getTenantCode());

            Authentication authentication = buildAuthentication(claims);

            SecurityContextHolder.getContext() .setAuthentication(authentication);

        } catch (TokenValidationException ex) {
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }

    private Authentication buildAuthentication(JwtAuthenticationClaims claims) {

        List<GrantedAuthority> authorities = claims.getRoles()
                .stream()
                .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toUnmodifiableList());

        UserDetails principal =new UserDetailsAdapter(claims.getUserId(),claims.getUsername(), claims.getTenantId(),claims.getTenantCode(),
                true,true, authorities);

        return new UsernamePasswordAuthenticationToken(principal,null,authorities);
    }



}

