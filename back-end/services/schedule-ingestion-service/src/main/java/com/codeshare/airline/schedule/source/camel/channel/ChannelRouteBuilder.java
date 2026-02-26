package com.codeshare.airline.schedule.source.camel.channel;

import com.codeshare.airline.schedule.domain.common.ScheduleSourceType;
import com.codeshare.airline.schedule.source.persistence.entity.AirlineIngestionChannel;
import org.apache.camel.builder.RouteBuilder;

public interface ChannelRouteBuilder {

    /**
     * Which source type this builder supports (SFTP, MQ, EMAIL, etc.)
     */
    ScheduleSourceType supports();

    /**
     * Build the Camel route for the given channel.
     */
    void build(RouteBuilder routeBuilder,
               AirlineIngestionChannel channel);
}