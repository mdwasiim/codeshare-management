package com.codeshare.airline.identity.access.authentication.core.security.filter;

import com.codeshare.airline.identity.access.authentication.core.domain.TenantContext;
import com.codeshare.airline.identity.access.authentication.core.domain.TenantContextHolder;
import com.codeshare.airline.identity.access.authentication.core.service.core.TenantContextResolver;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE+1)
public class TenantHeaderFilter extends OncePerRequestFilter {

    private final TenantContextResolver tenantContextResolver;

    public TenantHeaderFilter(TenantContextResolver tenantContextResolver) {
        this.tenantContextResolver = tenantContextResolver;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        boolean skip = path.startsWith("/.well-known/") ||  path.startsWith("/auth/");
        if (skip) {
            log.debug("Skipping TenantHeaderFilter for path: {}", path);
        }

        return skip;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,FilterChain filterChain ) throws IOException, ServletException {

        String path = request.getRequestURI();
        String tenantCode = request.getHeader("X-Tenant-Id");

        log.debug("TenantHeaderFilter invoked for path: {}", path);

        if (tenantCode == null || tenantCode.isBlank()) {
            log.warn( "Missing tenant-code header | method={} path={} remoteAddr={}", request.getMethod(), path,request.getRemoteAddr());

            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"Missing tenant-code");
            return;
        }

        try {
            log.debug("Resolving ingestion context for tenant-code: {}", tenantCode);

            TenantContext tenant = tenantContextResolver.resolveTenant(tenantCode);
            TenantContextHolder.setTenant(tenant);
            log.info("Tenant context resolved successfully for tenant-code: {}", tenantCode);
        } catch (Exception ex) {
            log.error("Failed to resolve tenant context for tenant-code: {} | path={}",tenantCode,path,ex);
            throw ex;
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContextHolder.clear(); // 🔥 REQUIRED
        }
    }
}
