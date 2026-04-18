package com.codeshare.airline.kafka.consumer;

import com.codeshare.airline.kafka.idempotency.IdempotencyStore;
import com.codeshare.airline.kafka.model.BaseEvent;
import com.codeshare.airline.kafka.tracing.KafkaMdcUtil;
import com.codeshare.airline.kafka.tracing.KafkaTraceHeaders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;

import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractKafkaEventConsumer<T extends BaseEvent> {

    private final IdempotencyStore idempotencyStore;


    protected abstract void handle(T event);

    protected boolean isDuplicate(T event) {
        return false; // override in service if needed
    }

    protected void beforeHandle(T event) {
        // metrics, tracing, MDC
    }

    protected void afterHandle(T event) {
        // metrics
    }

    protected void onError(T event, Exception ex) {
        log.error(
                "Kafka consume failed | eventId={} | type={}",
                event.getEventId(),
                event.getClass().getSimpleName(),
                ex
        );
    }

    protected String idempotencyKey(T event) {
        return event.getIdempotencyKey() != null
                ? event.getIdempotencyKey()
                : event.getEventId();
    }


    public final void consume(ConsumerRecord<String, T> record) {

        Headers headers = record.headers();

        String traceId = headerValue(headers, KafkaTraceHeaders.TRACE_ID);
        String tenantId = headerValue(headers, KafkaTraceHeaders.TENANT_ID);

        try {
            KafkaMdcUtil.put(traceId, tenantId);
            handle(record.value());
        } finally {
            KafkaMdcUtil.clear();
        }
    }

    private String headerValue(Headers headers, String name) {

        Header header = headers.lastHeader(name);
        return header == null
                ? null
                : new String(header.value(), StandardCharsets.UTF_8);
    }
}

