package com.codeshare.airline.gateway.exception;


import com.codeshare.airline.common.services.response.ServiceError;
import com.codeshare.airline.common.services.response.ServiceResponse;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Order(-2)
public class GatewayErrorHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ServiceError error = new ServiceError(
                "GATEWAY_ERROR",
                ex.getMessage(),
                "Unexpected error in Gateway"
        );

        ServiceResponse<Object> response = ServiceResponse.error(error);

        byte[] bytes = response.toString().getBytes();

        return exchange.getResponse().writeWith(
                Mono.just(exchange.getResponse()
                        .bufferFactory()
                        .wrap(bytes)
                )
        );
    }
}
