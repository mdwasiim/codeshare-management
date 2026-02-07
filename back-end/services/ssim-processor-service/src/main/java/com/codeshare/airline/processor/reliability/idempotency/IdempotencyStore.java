package com.codeshare.airline.processor.reliability.idempotency;

public interface IdempotencyStore {

    boolean isProcessed(String key);

    void markProcessed(String key);
}
