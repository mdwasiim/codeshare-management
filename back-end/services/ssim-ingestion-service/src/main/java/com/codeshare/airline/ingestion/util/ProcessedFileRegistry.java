package com.codeshare.airline.ingestion.util;

import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ProcessedFileRegistry {

    private final Set<String> processedFiles = ConcurrentHashMap.newKeySet();

    public boolean alreadyProcessed(String fileHash) {
        return processedFiles.contains(fileHash);
    }

    public void markProcessed(String fileHash) {
        processedFiles.add(fileHash);
    }
}
