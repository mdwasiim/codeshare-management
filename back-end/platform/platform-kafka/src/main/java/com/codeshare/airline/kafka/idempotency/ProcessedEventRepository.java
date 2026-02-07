package com.codeshare.airline.kafka.idempotency;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedEventRepository  extends JpaRepository<ProcessedEventEntity, String> {
    boolean existsById(String key);
}
