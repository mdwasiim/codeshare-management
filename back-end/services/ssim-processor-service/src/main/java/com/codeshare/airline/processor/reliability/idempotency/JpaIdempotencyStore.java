package com.codeshare.airline.processor.reliability.idempotency;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class JpaIdempotencyStore implements IdempotencyStore {

    private final ProcessedEventRepository repository;

    public JpaIdempotencyStore(ProcessedEventRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isProcessed(String key) {
        return repository.existsById(key);
    }

    @Override
    public void markProcessed(String key) {
        try {
            repository.save(new ProcessedEvent(key, Instant.now()));
        } catch (DataIntegrityViolationException ignored) {
            // duplicate → already processed
        }
    }
}

/* ---------- JPA ---------- */

@Entity
@Table(name = "processed_events")
@NoArgsConstructor
@AllArgsConstructor
class ProcessedEvent {

    @Id
    private String idempotencyKey;

    private Instant processedAt;
}

interface ProcessedEventRepository
        extends JpaRepository<ProcessedEvent, String> {}
