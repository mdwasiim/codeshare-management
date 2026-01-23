package com.codeshare.airline.gateway.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Component
public class RequestTracingGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        long startTime = System.currentTimeMillis();

        String txId = exchange.getRequest().getHeaders().getFirst("X-Transaction-Id");
        if (txId == null) {
            txId = UUID.randomUUID().toString();
        }

        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                .header("X-Transaction-Id", txId)
                .header("X-Request-Start-Time", String.valueOf(startTime))
                .build();

        String finalTxId = txId;
        return chain.filter(exchange)
                .doFinally(signalType -> {
                    if (!exchange.getResponse().isCommitted()) {
                        long duration = System.currentTimeMillis() - startTime;
                        exchange.getResponse()
                                .getHeaders()
                                .add("X-Response-Time", duration + "ms");
                    }
                });

    }

    @Override
    public int getOrder() {
        return -1;
    }
}
