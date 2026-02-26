package com.codeshare.airline.schedule.source.camel.channel;

import com.codeshare.airline.schedule.domain.common.ScheduleSourceType;
import com.codeshare.airline.schedule.source.persistence.entity.AirlineIngestionChannel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import static org.apache.camel.builder.Builder.constant;

@Component
public class EmailChannelRouteBuilder implements ChannelRouteBuilder {

    @Override
    public ScheduleSourceType supports() {
        return ScheduleSourceType.EMAIL;
    }

    @Override
    public void build(RouteBuilder rb,
                      AirlineIngestionChannel channel) {

        if (channel.getHost() == null ||
                channel.getHost().isBlank()) {
            throw new IllegalStateException("Email host is required");
        }

        String uri = String.format(
                "imaps://%s?username=%s&delete=false",
                channel.getHost(),
                channel.getUsername()
        );

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

    private String buildRouteId(AirlineIngestionChannel channel) {
        return channel.getProfile().getAirlineCode()
                + "-"
                + channel.getSourceType()
                + "-"
                + channel.getMessageType();
    }
}