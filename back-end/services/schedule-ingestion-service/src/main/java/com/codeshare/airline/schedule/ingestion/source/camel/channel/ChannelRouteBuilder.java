package com.codeshare.airline.schedule.ingestion.source.camel.channel;

import com.codeshare.airline.platform.core.enums.schedule.SourceType;
import com.codeshare.airline.schedule.ingestion.dto.source.AirlineIngestionChannelDTO;
import com.codeshare.airline.schedule.ingestion.dto.source.AirlineIngestionProfileDTO;
import org.apache.camel.builder.RouteBuilder;

public interface ChannelRouteBuilder {

    /**
     * Which source type this builder supports (SFTP, MQ, EMAIL, etc.)
     */
    SourceType supports();

    /**
     * Build the Camel route for the given channel.
     */
    void build(RouteBuilder routeBuilder, AirlineIngestionProfileDTO profile, AirlineIngestionChannelDTO channel);
}
