package com.codeshare.airline.kafka.idempotency;

import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryIdempotencyStore implements IdempotencyStore {

    private final Set<String> processedKeys =
            ConcurrentHashMap.newKeySet();

    @Override
    public boolean isProcessed(String key) {
        return processedKeys.contains(key);
    }

    @Override
    public void markProcessed(String key) {
        processedKeys.add(key);
    }
}
