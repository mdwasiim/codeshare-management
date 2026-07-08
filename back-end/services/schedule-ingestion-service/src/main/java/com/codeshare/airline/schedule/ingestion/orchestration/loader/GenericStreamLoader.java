package com.codeshare.airline.schedule.ingestion.orchestration.loader;

import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.schedule.ingestion.orchestration.handler.StreamExtractorHandler;
import com.codeshare.airline.schedule.ingestion.orchestration.flow.BlockTaskCoordinator;
import com.codeshare.airline.schedule.ingestion.orchestration.flow.MessageBlockProcessor;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public abstract class GenericStreamLoader {

    private final Map<MessageType, StreamExtractorHandler> extractorMap;
    private final MessageBlockProcessor blockProcessor;
    private final BlockTaskCoordinator taskCoordinator;

    protected GenericStreamLoader(
            Map<MessageType, StreamExtractorHandler> extractorMap,
            MessageBlockProcessor blockProcessor,
            BlockTaskCoordinator taskCoordinator
    ) {
        this.extractorMap = extractorMap;
        this.blockProcessor = blockProcessor;
        this.taskCoordinator = taskCoordinator;
    }

    public boolean processStream(InputStream inputStream, ScheduleFileMetaDataDTO metadata, MessageType type) {

        if (!supports(type)) {
            throw new IllegalStateException("Unsupported message type " + type);
        }

        StreamExtractorHandler extractor = extractorMap.get(type);
        if (extractor == null) {
            throw new IllegalStateException("No extractor for type=" + type);
        }

        AtomicBoolean hasErrors = new AtomicBoolean(false);
        List<CompletableFuture<Void>> futures = taskCoordinator.newFutureList();
        long start = System.currentTimeMillis();

        extractor.extract(inputStream, lines -> {
            Runnable task = () -> blockProcessor.process(lines, metadata, type, hasErrors);
            taskCoordinator.execute(extractor.isParallelSafe(), task, futures);
        });

        taskCoordinator.awaitAll(futures);

        long duration = System.currentTimeMillis() - start;
        log.info("Stream processed | type={} | durationMs={} | hasErrors={}",
                type, duration, hasErrors.get());

        return hasErrors.get();
    }

    protected abstract boolean supports(MessageType type);
}
