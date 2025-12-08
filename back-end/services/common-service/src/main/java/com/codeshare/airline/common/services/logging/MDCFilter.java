/*
package com.codeshare.airline.common.services.logging;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;

import java.io.IOException;

public class MDCFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        MDC.put("traceId", req.getHeader("X-Trace-Id"));
        MDC.put("tenantId", req.getHeader("X-Tenant-Id"));
        MDC.put("userId", req.getHeader("X-User-Id"));

        try {
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
*/
