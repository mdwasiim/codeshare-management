package com.codeshare.airline.gateway.exception;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

public final class GatewayErrorWriter {

    private GatewayErrorWriter() {}

    public static Mono<Void> write(
            ServerHttpResponse response,
            HttpStatus status,
            String error,
            String message,
            ServerWebExchange exchange) {

        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String correlationId =
                exchange.getRequest().getHeaders().getFirst("X-Correlation-Id");

        String body = """
        {
          "timestamp": "%s",
          "status": %d,
          "error": "%s",
          "message": "%s",
          "path": "%s",
          "correlationId": "%s"
        }
        """.formatted(
                Instant.now(),
                status.value(),
                error,
                message,
                exchange.getRequest().getPath(),
                correlationId
        );

        DataBuffer buffer =
                response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));

        return response.writeWith(Mono.just(buffer));
    }
}
