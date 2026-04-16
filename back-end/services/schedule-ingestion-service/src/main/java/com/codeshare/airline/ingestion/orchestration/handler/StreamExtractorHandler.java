package com.codeshare.airline.ingestion.orchestration.handler;

import com.codeshare.airline.core.enums.MessageType;

import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;

public interface StreamExtractorHandler {

    MessageType supportedType();

    void extract(InputStream is, Consumer<List<String>> consumer);

    default boolean isParallelSafe() { return false; }

    default boolean isBatchSupported() { return false; }
}