package com.codeshare.airline.schedule.ingestion.source.camel.route;

import com.codeshare.airline.schedule.ingestion.orchestration.ScheduleMessageIngestionProcessor;
import com.codeshare.airline.schedule.ingestion.orchestration.SsimDatasetIngestionProcessor;
import com.codeshare.airline.schedule.ingestion.shared.exceptions.BusinessValidationException;
import com.codeshare.airline.schedule.ingestion.source.camel.mapper.ScheduleSourceExchangeMapper;
import com.codeshare.airline.schedule.ingestion.source.model.ScheduleSourceFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(1)
@RequiredArgsConstructor
public class CoreIngestionRouteBuilder extends RouteBuilder {

    private final ScheduleSourceExchangeMapper exchangeMapper;
    private final ScheduleMessageIngestionProcessor scheduleMessageIngestionProcessor;
    private final SsimDatasetIngestionProcessor ssimDatasetIngestionProcessor;

    @Override
    public void configure() {

        onException(BusinessValidationException.class)
                .routeId("INGESTION-BUSINESS-ERROR")
                .maximumRedeliveries(0)
                .process(e -> e.getMessage().setHeader("ERROR",
                        e.getProperty(Exchange.EXCEPTION_CAUGHT)))
                .log(" Ingestion validation error: ${exception.message}")
                .handled(false);

        onException(Exception.class)
                .routeId("INGESTION-GLOBAL-ERROR")
                .maximumRedeliveries(3)
                .redeliveryDelay(2000)
                .process(e -> e.getMessage().setHeader("ERROR",
                        e.getProperty(Exchange.EXCEPTION_CAUGHT)))
                .log(" Ingestion error: ${exception.message}")
                .handled(false);

        from("seda:dead-letter")
                .routeId("DLQ-CORE")
                .log(" Dead-letter triggered airline=${header.AIRLINE_CODE}");

        buildRoute("direct:schedule-message-processing",
                "PROCESS-SCHEDULE-MESSAGE-CORE", "SCHEDULE_MESSAGE", scheduleMessageIngestionProcessor);

        buildRoute("direct:ssim-dataset-processing",
                "PROCESS-SSIM-DATASET-CORE", "SSIM_DATASET", ssimDatasetIngestionProcessor);
    }

    private void buildRoute(String endpoint, String routeId, String type, Processor processor) {

        from(endpoint)
                .routeId(routeId)
                .log(" [${header.AIRLINE_CODE}] " + type + " PROCESSING file=${header.CamelFileName}")
                .process(this::map)
                .process(processor)
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
