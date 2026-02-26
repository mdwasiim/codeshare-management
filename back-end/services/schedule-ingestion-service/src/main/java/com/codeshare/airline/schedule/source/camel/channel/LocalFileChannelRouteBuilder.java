package com.codeshare.airline.schedule.source.camel.channel;

import com.codeshare.airline.schedule.domain.common.ScheduleSourceType;
import com.codeshare.airline.schedule.source.persistence.entity.AirlineIngestionChannel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import static org.apache.camel.builder.Builder.constant;

@Component
public class LocalFileChannelRouteBuilder implements ChannelRouteBuilder {

    @Override
    public ScheduleSourceType supports() {
        return ScheduleSourceType.LOCAL;
    }

    @Override
    public void build(RouteBuilder rb,
                      AirlineIngestionChannel channel) {

        if (channel.getRemoteDirectory() == null ||
                channel.getRemoteDirectory().isBlank()) {
            throw new IllegalStateException("Local directory is required");
        }

        String uri = "file:" + channel.getRemoteDirectory();

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