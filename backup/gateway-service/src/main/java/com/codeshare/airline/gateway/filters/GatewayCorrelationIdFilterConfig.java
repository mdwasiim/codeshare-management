package com.codeshare.airline.gateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class GatewayCorrelationIdFilterConfig implements GlobalFilter, Ordered {

    public static final String CORRELATION_ID = "X-Correlation-Id";
    public static final String TENANT_ID = "X-Tenant-Id";
    public static final String USER_ID = "X-User-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();

        String incomingCorrelationId =
                request.getHeaders().getFirst(CORRELATION_ID);

        final String correlationId =
                (incomingCorrelationId == null || incomingCorrelationId.isBlank())
                        ? UUID.randomUUID().toString()
                        : incomingCorrelationId;

        ServerHttpRequest.Builder mutatedRequest = request.mutate()
                .header(CORRELATION_ID, correlationId);

        copyIfPresent(request, mutatedRequest, TENANT_ID);
        copyIfPresent(request, mutatedRequest, USER_ID);

        ServerWebExchange mutatedExchange =
                exchange.mutate()
                        .request(mutatedRequest.build())
                        .build();

        // Add to response
        mutatedExchange.getResponse()
                .getHeaders()
                .add(CORRELATION_ID, correlationId);

        return chain
                .filter(mutatedExchange)
                .contextWrite(ctx -> ctx.put(CORRELATION_ID, correlationId));
    }


    private void copyIfPresent(ServerHttpRequest request,
                               ServerHttpRequest.Builder builder,
                               String header) {

        String value = request.getHeaders().getFirst(header);
        if (value != null && !value.isBlank()) {
            builder.header(header, value);
        }
    }
    /**
     * Must run BEFORE security, tracing, logging
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
