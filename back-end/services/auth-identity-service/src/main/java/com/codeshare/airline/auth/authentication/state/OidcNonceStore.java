package com.codeshare.airline.auth.authentication.state;


import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OidcNonceStore {

    private static final long TTL_SECONDS = 300; // 5 minutes

    private final Map<String, Instant> usedNonces = new ConcurrentHashMap<>();

    public boolean isUsed(String nonce) {
        cleanup();
        return usedNonces.containsKey(nonce);
    }

    public void markUsed(String nonce) {
        usedNonces.put(nonce, Instant.now());
    }

    private void cleanup() {
        Instant now = Instant.now();
        usedNonces.entrySet().removeIf(
                e -> e.getValue().plusSeconds(TTL_SECONDS).isBefore(now)
        );
    }
}

