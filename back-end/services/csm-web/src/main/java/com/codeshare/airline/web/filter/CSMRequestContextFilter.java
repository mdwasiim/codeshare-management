package com.codeshare.airline.web.filter;

import com.codeshare.airline.core.dto.audit.context.CSMAuditContext;
import com.codeshare.airline.core.response.CSMRequestTimeProvider;
import com.codeshare.airline.core.utils.CSMTransactionIdProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component("csmRequestContextFilter")
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class CSMRequestContextFilter extends OncePerRequestFilter {

    public static final String TXN_HEADER = "X-Transaction-Id";
    public static final String USER_HEADER = "X-User-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse resp,
                                    FilterChain chain)
            throws ServletException, IOException {
        log.info("RequestContextFilter invoked for {}", req.getRequestURI());
        CSMRequestTimeProvider.start();

        String txnId = Optional.ofNullable(req.getHeader(TXN_HEADER))
                .filter(s -> !s.isBlank())
                .orElse(UUID.randomUUID().toString());

        String user = Optional.ofNullable(req.getHeader(USER_HEADER))
                .filter(s -> !s.isBlank())
                .orElse("SYSTEM");

        CSMAuditContext ctx = CSMAuditContext.get();
        ctx.setTransactionId(txnId);
        ctx.setUserId(user);

        CSMTransactionIdProvider.set(txnId);

        MDC.put("txnId", txnId);
        MDC.put("user", user);

        if (!resp.containsHeader(TXN_HEADER)) {
            resp.setHeader(TXN_HEADER, txnId);
        }

        try {
            chain.doFilter(req, resp);
        } finally {

            long timeTaken = CSMRequestTimeProvider.getTimeTaken();

            log.info("[txnId={}] {} {} completed in {} ms",
                    txnId,
                    req.getMethod(),
                    req.getRequestURI(),
                    timeTaken
            );

            CSMAuditContext.clear();
            CSMTransactionIdProvider.clear();
            CSMRequestTimeProvider.clear();

            MDC.remove("txnId");
            MDC.remove("user");
        }
    }
}