package com.codeshare.airline.common.httpTransaction;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class TransactionIdFilter implements Filter {

    public static final String HEADER = "X-Transaction-Id";
    public static final String MDC_TXN_KEY = "txnId";
    public static final String MDC_USER_KEY = "user";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String incoming = req.getHeader(HEADER);
        // Use numeric transaction ID
        String txnId = (incoming == null || incoming.isBlank()) ? TransactionIdGenerator.nextId("TXN") : incoming;

        // set thread-local and MDC for logs
        TransactionIdProvider.set(txnId);
        MDC.put(MDC_TXN_KEY, txnId);

        // If a security filter populated username into AuditUserProvider, expose to MDC
        String user = AuditUserProvider.get();
        if (user != null) MDC.put(MDC_USER_KEY, user);

        // also set header in response for client visibility
        resp.setHeader(HEADER, txnId);

        try {
            chain.doFilter(request, response);
        } finally {
            TransactionIdProvider.clear();
            MDC.remove(MDC_TXN_KEY);
            MDC.remove(MDC_USER_KEY);
        }
    }
}
