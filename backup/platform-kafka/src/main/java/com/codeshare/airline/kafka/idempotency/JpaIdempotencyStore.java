package com.codeshare.airline.kafka.idempotency;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JpaIdempotencyStore implements IdempotencyStore {

    //private final ProcessedEventRepository repository;

    /**
     * Simple existence check.
     * Safe because DB enforces uniqueness.
     */
    @Override
    public boolean isProcessed(String key) {
       // return repository.existsById(key);
        return true;
    }

    /**
     * Insert-only.
     * Duplicate insert = already processed.
     */
    @Override
    public void markProcessed(String key) {
        /*try {
            repository.save(
                    ProcessedEventEntity.builder()
                            .idempotencyKey(key)
                            .processedAt(Instant.now())
                            .build()
            );
        } catch (DataIntegrityViolationException ex) {
            // Duplicate key → retry / replay / parallel consumer
            log.warn("🔁 Idempotency key already processed: {}", key);
        }*/
    }
}
