package com.codeshare.airline.kafka.idempotency;

public interface IdempotencyStore {

    boolean isProcessed(String key);

    void markProcessed(String key);
}
