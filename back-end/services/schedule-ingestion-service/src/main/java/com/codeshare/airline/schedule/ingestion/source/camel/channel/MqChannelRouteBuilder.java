package com.codeshare.airline.schedule.ingestion.source.camel.channel;

import com.codeshare.airline.platform.core.enums.schedule.SourceType;
import com.codeshare.airline.schedule.ingestion.dto.source.AirlineIngestionChannelDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MqChannelRouteBuilder extends AbstractChannelRouteBuilder {

    @Override
    public SourceType supports() {
        return SourceType.MQ;
    }

    @Override
    protected String buildUri(AirlineIngestionChannelDTO c) {

        return new StringBuilder("activemq:queue:" + c.getQueueName())
                .append("?concurrentConsumers=").append(val(c.getConcurrentConsumers(), 1))
                .append("&maxConcurrentConsumers=").append(val(c.getMaxConcurrentConsumers(), 5))
                .append("&asyncConsumer=").append(val(c.getAsyncConsumer(), true))
                .append("&receiveTimeout=").append(val(c.getReceiveTimeoutMs(), 1000))
                .append("&maxMessagesPerTask=").append(val(c.getMaxMessagesPerTask(), 10))
                .append("&bridgeErrorHandler=").append(val(c.getBridgeErrorHandler(), true))
                .toString();
    }

    @Override
    protected void validate(AirlineIngestionChannelDTO c) {
        if (c.getQueueName() == null || c.getQueueName().isBlank()) {
            throw new IllegalStateException("MQ queue name is required");
        }
    }
}
