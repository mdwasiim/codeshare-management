package com.codeshare.airline.common.httpTransaction;

import com.codeshare.airline.common.response.common.RequestTimeProvider;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TransactionIdFilter implements Filter {

    public static final String HEADER = "X-Transaction-Id";
    public static final String MDC_TXN_KEY = "txnId";
    public static final String MDC_USER_KEY = "user";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Start a per-request timer
        RequestTimeProvider.start();

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // Read Txn ID from incoming header
        String incoming = req.getHeader(HEADER);
        String txnId = (incoming == null || incoming.isBlank())
                ? TransactionIdGenerator.nextId("TXN")
                : incoming;

        // ThreadLocal + MDC
        TransactionIdProvider.set(txnId);
        MDC.put(MDC_TXN_KEY, txnId);

        // Add authenticated user (if already set)
        String user = AuditUserProvider.get();
        if (user != null) {
            MDC.put(MDC_USER_KEY, user);
        }

        // Return txnId to client
        resp.setHeader(HEADER, txnId);

        try {
            chain.doFilter(request, response);
        } finally {
            // **** IMPORTANT: clear all ThreadLocal data ****
            RequestTimeProvider.clear();
            TransactionIdProvider.clear();
            AuditUserProvider.clear();  // <-- REQUIRED FIX

            // clean logs
            MDC.remove(MDC_TXN_KEY);
            MDC.remove(MDC_USER_KEY);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void destroy() {}
}
