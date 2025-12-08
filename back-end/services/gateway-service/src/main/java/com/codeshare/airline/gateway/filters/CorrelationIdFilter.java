package com.codeshare.airline.gateway.filters;


import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class CorrelationIdFilter implements GlobalFilter, Ordered {


    private static final String TRACE_ID = "X-Trace-Id";
    private static final String TENANT_ID = "X-Tenant-Id";
    private static final String USER_ID = "X-User-Id";


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {


        ServerHttpRequest request = exchange.getRequest();


        String traceId = request.getHeaders().getFirst(TRACE_ID);
        if (traceId == null || traceId.isBlank()) {
            traceId = UUID.randomUUID().toString();
        }


        String tenantId = request.getHeaders().getFirst(TENANT_ID);
        String userId = request.getHeaders().getFirst(USER_ID);


        ServerHttpRequest mutated = request.mutate()
                .header(TRACE_ID, traceId)
                .header(TENANT_ID, tenantId == null ? "" : tenantId)
                .header(USER_ID, userId == null ? "" : userId)
                .build();


        MDC.put("traceId", traceId);
        MDC.put("tenantId", tenantId);
        MDC.put("userId", userId);


        return chain.filter(exchange.mutate().request(mutated).build())
                .doFinally(signal -> MDC.clear());
    }

    @Override
    public int getOrder() {
        return -10;
    }
}