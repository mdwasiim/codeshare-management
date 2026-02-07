package com.codeshare.airline.kafka.producer;

import com.codeshare.airline.kafka.model.BaseEvent;
import com.codeshare.airline.kafka.tracing.KafkaTraceHeaders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractKafkaEventProducer<T extends BaseEvent> {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    /** Each producer owns exactly one topic */
    protected abstract String topic();

    /** Override for domain-specific partitioning */
    protected String key(T event) {
        return event.getEventId();
    }

    public void publish(T event) {

        // -------- Trace enforcement --------
        if (event.getTraceId() == null) {
            event.setTraceId(UUID.randomUUID().toString());
        }

        ProducerRecord<String, Object> record =
                new ProducerRecord<>(topic(), key(event), event);

        // -------- Header propagation --------
        record.headers().add(
                KafkaTraceHeaders.TRACE_ID,
                event.getTraceId().getBytes(StandardCharsets.UTF_8)
        );

        if (event.getTenantId() != null) {
            record.headers().add(
                    KafkaTraceHeaders.TENANT_ID,
                    event.getTenantId().getBytes(StandardCharsets.UTF_8)
            );
        }

        if (event.getSource() != null) {
            record.headers().add(
                    KafkaTraceHeaders.SOURCE,
                    event.getSource().getBytes(StandardCharsets.UTF_8)
            );
        }

        // -------- Async send with hooks --------
        kafkaTemplate.send(record)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        onSuccess(event);
                    } else {
                        onFailure(event, ex);
                    }
                });
    }

    /**
     * Hook for metrics / audit logging
     */
    protected void onSuccess(T event) {
        log.debug(
                "Kafka publish success | topic={} eventId={}",
                topic(),
                event.getEventId()
        );
    }

    /**
     * Hook for alerting / failure metrics
     * NOTE: do NOT throw here unless you want ingestion to fail
     */
    protected void onFailure(T event, Throwable ex) {
        log.error(
                "Kafka publish failed | topic={} eventId={}",
                topic(),
                event.getEventId(),
                ex
        );
    }
}
