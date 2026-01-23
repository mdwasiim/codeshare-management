package com.codeshare.airline.auth.common;

import com.codeshare.airline.core.dto.audit.context.CSMAuditUserContext;
import com.codeshare.airline.core.response.CSMRequestTimeProvider;
import com.codeshare.airline.core.utils.CSMTransactionIdGenerator;
import com.codeshare.airline.core.utils.CSMTransactionIdProvider;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorrelationIdFilter implements Filter {

    public static final String HEADER = "X-Transaction-Id";
    public static final String MDC_TXN_KEY = "txnId";
    public static final String MDC_USER_KEY = "user";

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        CSMRequestTimeProvider.start();

        if (!(request instanceof HttpServletRequest req) ||
                !(response instanceof HttpServletResponse resp)) {
            chain.doFilter(request, response);
            return;
        }

        String incoming = req.getHeader(HEADER);
        String txnId = (incoming == null || incoming.isBlank())
                ? CSMTransactionIdGenerator.nextId("TXN")
                : incoming;

        CSMTransactionIdProvider.set(txnId);
        MDC.put(MDC_TXN_KEY, txnId);

        String user = CSMAuditUserContext.get();
        if (user != null) {
            MDC.put(MDC_USER_KEY, user);
        }

        resp.setHeader(HEADER, txnId);

        try {
            chain.doFilter(request, response);
        } finally {
            CSMRequestTimeProvider.clear();
            CSMTransactionIdProvider.clear();
            CSMAuditUserContext.clear();

            MDC.remove(MDC_TXN_KEY);
            MDC.remove(MDC_USER_KEY);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}
}
