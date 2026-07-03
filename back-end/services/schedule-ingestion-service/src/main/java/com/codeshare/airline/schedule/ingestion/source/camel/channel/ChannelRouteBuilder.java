package com.codeshare.airline.schedule.ingestion.source.camel.channel;

import com.codeshare.airline.schedule.ingestion.domain.enums.SourceType;
import com.codeshare.airline.schedule.ingestion.persistence.entities.source.ScheduleIngestionChannelEntity;
import org.apache.camel.builder.RouteBuilder;

public interface ChannelRouteBuilder {

    /**
     * Which source type this builder supports (SFTP, MQ, EMAIL, etc.)
     */
    SourceType supports();

    /**
     * Build the Camel route for the given channel.
     */
    void build(RouteBuilder routeBuilder, ScheduleIngestionChannelEntity channel);
}