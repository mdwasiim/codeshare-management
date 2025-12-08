package com.codeshare.airline.gateway.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GlobalGatewayFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {

        long startTime = System.currentTimeMillis();
        String txId = exchange.getRequest().getHeaders().getFirst("X-Transaction-Id");

        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    long duration = System.currentTimeMillis() - startTime;

                    log.info("[GATEWAY] [{}] {} {} {}ms",
                            txId,
                            exchange.getRequest().getMethod(),
                            exchange.getRequest().getURI(),
                            duration
                    );

                    exchange.getResponse().getHeaders().add("X-Response-Time", duration + "ms");
                }));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
