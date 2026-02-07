package com.codeshare.airline.kafka.idempotency;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class JpaIdempotencyStore implements IdempotencyStore {

    private final ProcessedEventRepository repository;

    /**
     * ⚠️ Do NOT check first.
     * Insert-only approach is race-condition safe.
     */
    @Override
    public boolean isProcessed(String key) {
        try {
            return repository.existsById(key);
            // process event
        } catch (DataIntegrityViolationException ex) {
            // skip duplicate
        }
        return repository.existsById(key);
    }

    @Override
    public void markProcessed(String key) {
        try {
            repository.save(
                    ProcessedEventEntity.builder()
                            .idempotencyKey(key)
                            .processedAt(Instant.now())
                            .build()
            );
        } catch (DataIntegrityViolationException ex) {
            // Duplicate key → already processed (retry / replay)
            log.warn("🔁 Idempotency key already processed: {}", key);
        }
    }
}
