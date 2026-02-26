package com.codeshare.airline.schedule.source.camel.channel;

import com.codeshare.airline.schedule.domain.common.ScheduleSourceType;
import com.codeshare.airline.schedule.source.persistence.entity.AirlineIngestionChannel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import static org.apache.camel.builder.Builder.constant;

@Component
public class MqChannelRouteBuilder implements ChannelRouteBuilder {

    @Override
    public ScheduleSourceType supports() {
        return ScheduleSourceType.MQ;
    }

    @Override
    public void build(RouteBuilder rb,
                      AirlineIngestionChannel channel) {

        validate(channel);

        String uri = buildUri(channel);

        String routeId = buildRouteId(channel);

        rb.from(uri)
                .routeId(routeId)
                .setHeader("AIRLINE_CODE",
                        constant(channel.getProfile().getAirlineCode()))
                .setHeader("SOURCE_TYPE",
                        constant(channel.getSourceType().name()))
                .setHeader("MESSAGE_TYPE",
                        constant(channel.getMessageType().name()))
                .to("direct:schedule-processing");
    }

    private String buildUri(AirlineIngestionChannel channel) {

        // If brokerUrl is defined, use full connection URI
        if (channel.getBrokerUrl() != null &&
                !channel.getBrokerUrl().isBlank()) {

            return String.format(
                    "activemq:queue:%s?brokerURL=%s&concurrentConsumers=%d",
                    channel.getQueueName(),
                    channel.getBrokerUrl(),
                    resolveConcurrentConsumers(channel)
            );
        }

        // Fallback to default ActiveMQ component
        return String.format(
                "activemq:queue:%s?concurrentConsumers=%d",
                channel.getQueueName(),
                resolveConcurrentConsumers(channel)
        );
    }

    private int resolveConcurrentConsumers(AirlineIngestionChannel channel) {

        if (channel.getConnectionTimeoutMs() != null) {
            return Math.max(1, channel.getConnectionTimeoutMs() / 10000);
        }

        return 1;
    }

    private void validate(AirlineIngestionChannel channel) {

        if (channel.getQueueName() == null ||
                channel.getQueueName().isBlank()) {
            throw new IllegalStateException(
                    "MQ queue name is required for channel "
                            + channel.getId());
        }
    }

    private String buildRouteId(AirlineIngestionChannel channel) {

        return channel.getProfile().getAirlineCode()
                + "-"
                + channel.getSourceType()
                + "-"
                + channel.getMessageType();
    }
}