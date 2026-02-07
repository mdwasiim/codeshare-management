package com.codeshare.airline.processor.consumer;

import com.codeshare.airline.kafka.consumer.AbstractKafkaEventConsumer;
import com.codeshare.airline.processor.domain.event.SsimFlightBlockReceivedEvent;
import com.codeshare.airline.processor.processing.SsimFlightProcessor;
import com.codeshare.airline.processor.reliability.idempotency.IdempotencyStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SsimFlightBlockKafkaConsumer
        extends AbstractKafkaEventConsumer<SsimFlightBlockReceivedEvent> {

    private final SsimFlightProcessor processor;
    private final IdempotencyStore idempotencyStore;

    @Override
    protected void handle(SsimFlightBlockReceivedEvent event) {

        String idempotencyKey = event.getFlightFingerprint();

        // 🔐 Exactly-once effect
        if (idempotencyStore.isProcessed(idempotencyKey)) {
            log.info(
                    "Skipping already processed flight. fingerprint={}, flight={}",
                    idempotencyKey,
                    event.getFlightNumber()
            );
            return;
        }

        processor.process(event);

        idempotencyStore.markProcessed(idempotencyKey);
    }
}
