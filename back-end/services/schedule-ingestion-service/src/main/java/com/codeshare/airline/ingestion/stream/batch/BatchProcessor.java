/*
package com.codeshare.airline.ingestion.stream.batch;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

@Slf4j
public class BatchProcessor<T> {

    private final int batchSize;
    private final List<T> buffer;
    private final BiConsumer<List<T>, Object> consumer;
    private final Object context;

    public BatchProcessor(int batchSize,
                          BiConsumer<List<T>, Object> consumer,
                          Object context) {
        this.batchSize = batchSize;
        this.consumer = consumer;
        this.context = context;
        this.buffer = new ArrayList<>(batchSize);
    }

    public void add(T item) {
        buffer.add(item);
        if (buffer.size() >= batchSize) {
            flush();
        }
    }

    public void flush() {
        if (!buffer.isEmpty()) {
            consumer.accept(new ArrayList<>(buffer), context);
            buffer.clear();
        }
    }
}*/
