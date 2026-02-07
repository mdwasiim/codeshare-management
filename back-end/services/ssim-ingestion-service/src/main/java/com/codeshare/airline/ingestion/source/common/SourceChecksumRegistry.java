package com.codeshare.airline.ingestion.source.common;

import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SourceChecksumRegistry {

    private final Set<String> seenChecksums =
            ConcurrentHashMap.newKeySet();

    public boolean alreadySeen(String checksum) {
        return seenChecksums.contains(checksum);
    }

    public void markSeen(String checksum) {
        seenChecksums.add(checksum);
    }
}
