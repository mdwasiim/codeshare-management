package com.codeshare.airline.schedule.source.camel.route;

import com.codeshare.airline.schedule.orchestration.ScheduleIngestionProcessor;
import com.codeshare.airline.schedule.source.camel.channel.ChannelRouteBuilder;
import com.codeshare.airline.schedule.source.camel.processor.ScheduleSourceExchangeMapper;
import com.codeshare.airline.schedule.source.persistence.entity.AirlineIngestionChannel;
import com.codeshare.airline.schedule.source.persistence.repository.AirlineIngestionProfileRepository;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class DynamicIngestionRouteBuilder extends RouteBuilder {

    private final AirlineIngestionProfileRepository profileRepository;
    private final List<ChannelRouteBuilder> channelBuilders;
    private final ScheduleSourceExchangeMapper exchangeMapper;
    private final ScheduleIngestionProcessor scheduleIngestionProcessor;

    @Override
    public void configure() {

        // 👇 1️⃣ Global error handling (PUT HERE)
        onException(Exception.class)
                .routeId("ingestion-global-error")
                .log("Ingestion error: ${exception.stacktrace}")
                .maximumRedeliveries(3)
                .redeliveryDelay(2000)
                .handled(false);

        // 👇 2️⃣ Dead letter route
        buildDeadLetterRoute();

        // 👇 3️⃣ Processing route
        buildProcessingRoute();

        // 👇 4️⃣ Dynamic channel routes
        profileRepository.findAllWithChannels()
                .forEach(profile -> {

                    if (!Boolean.TRUE.equals(profile.getEnabled())) {
                        return;
                    }

                    profile.getChannels()
                            .forEach(this::buildChannel);
                });
    }

    private void buildChannel(AirlineIngestionChannel channel) {

        channelBuilders.stream()
                .filter(builder ->
                        builder.supports() == channel.getSourceType())
                .findFirst()
                .ifPresent(builder ->
                        builder.build(this, channel));
    }

    private void buildProcessingRoute() {

        if (getContext().getRoute("schedule-processing") != null) {
            return;
        }

        from("direct:schedule-processing")
                .routeId("schedule-processing")
                .process(exchange -> {

                    var sourceFile = exchangeMapper.map(exchange);

                    scheduleIngestionProcessor.process(sourceFile);
                });
    }

    private void buildDeadLetterRoute() {

        if (getContext().getRoute("dead-letter-route") != null) {
            return;
        }

        from("direct:dead-letter")
                .routeId("dead-letter-route")
                .log("Dead-letter: Airline=${header.AIRLINE_CODE}, "
                        + "MessageType=${header.MESSAGE_TYPE}, "
                        + "Error=${exception.message}");
    }
}