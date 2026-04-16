/*
package com.codeshare.airline.ingestion.source.camel.channel;

import com.codeshare.airline.ingestion.domain.enums.SourceType;
import com.codeshare.airline.ingestion.persistence.entities.source.ScheduleIngestionChannelEntity;
import com.codeshare.airline.ingestion.source.security.ScheduleCredentialResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MqChannelRouteBuilder extends AbstractChannelRouteBuilder {

    private final ScheduleCredentialResolver resolver;

    @Override
    public SourceType supports() {
        return SourceType.MQ;
    }

    @Override
    protected String buildUri(ScheduleIngestionChannelEntity c) {

        return new StringBuilder("activemq:queue:" + c.getQueueName())

                .append("?concurrentConsumers=").append(val(c.getConcurrentConsumers(), 1))
                .append("&maxConcurrentConsumers=").append(val(c.getMaxConcurrentConsumers(), 5))
                .append("&asyncConsumer=").append(val(c.getAsyncConsumer(), true))
                .append("&receiveTimeout=").append(val(c.getReceiveTimeoutMs(), 1000))
                .append("&maxMessagesPerTask=").append(val(c.getMaxMessagesPerTask(), 10))

                .toString();
    }

    @Override
    protected void validate(ScheduleIngestionChannelEntity c) {
        if (c.getQueueName() == null || c.getQueueName().isBlank()) {
            throw new IllegalStateException("Queue name is required");
        }
    }
}
*/
