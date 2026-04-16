package com.codeshare.airline.ingestion.source.camel.route;

import com.codeshare.airline.ingestion.orchestration.ScheduleIngestionProcessor;
import com.codeshare.airline.ingestion.source.camel.mapper.ScheduleSourceExchangeMapper;
import com.codeshare.airline.ingestion.source.inbound.ScheduleSourceFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;

@Slf4j
@Component
@Order(1)
@RequiredArgsConstructor
public class CoreIngestionRouteBuilder extends RouteBuilder {

    private final ScheduleSourceExchangeMapper exchangeMapper;
    private final ScheduleIngestionProcessor scheduleIngestionProcessor;

    @Override
    public void configure() {

        onException(Exception.class)
                .routeId("INGESTION-GLOBAL-ERROR")
                .maximumRedeliveries(3)
                .redeliveryDelay(2000)
                .process(e -> e.getMessage().setHeader("ERROR",
                        e.getProperty(Exchange.EXCEPTION_CAUGHT)))
                .log(" Ingestion error: ${exception.message}")
                .to("seda:dead-letter")
                .handled(true);

        from("seda:dead-letter")
                .routeId("DLQ-CORE")
                .log(" Dead-letter triggered airline=${header.AIRLINE_CODE}");

        buildRoute("seda:ssm-processing?concurrentConsumers=5&size=1000",
                "PROCESS-SSM-CORE", "SSM");

        buildRoute("seda:asm-processing?concurrentConsumers=2&size=1000",
                "PROCESS-ASM-CORE", "ASM");

        buildRoute("seda:ssim-processing?concurrentConsumers=2&size=1000",
                "PROCESS-SSIM-CORE", "SSIM");
    }

    private void buildRoute(String endpoint, String routeId, String type) {

        from(endpoint)
                .routeId(routeId)
                .log(" [${header.AIRLINE_CODE}] " + type + " PROCESSING file=${header.CamelFileName}")
                .process(exchange -> exchange.setProperty("CHECKSUM_DIGEST",
                        MessageDigest.getInstance("SHA-256")))
                .process(this::map)
                .process(scheduleIngestionProcessor)
                .log(" [${header.AIRLINE_CODE}] " + type + " completed file=${header.CamelFileName}");
    }

    private void map(Exchange exchange) {
        ScheduleSourceFile sourceFile = exchangeMapper.map(exchange);

        if (sourceFile == null) {
            throw new IllegalStateException("Mapping failed - sourceFile is null");
        }

        exchange.getMessage().setBody(sourceFile);
    }
}