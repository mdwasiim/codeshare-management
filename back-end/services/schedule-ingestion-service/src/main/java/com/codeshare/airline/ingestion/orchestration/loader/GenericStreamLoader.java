package com.codeshare.airline.ingestion.orchestration.loader;

import com.codeshare.airline.core.enums.MessageType;
import com.codeshare.airline.ingestion.domain.context.AbstractIngestionContext;
import com.codeshare.airline.ingestion.domain.context.AsmIngestionContext;
import com.codeshare.airline.ingestion.domain.context.SsimIngestionContext;
import com.codeshare.airline.ingestion.domain.context.SsmIngestionContext;
import com.codeshare.airline.ingestion.domain.enums.ValidationStage;
import com.codeshare.airline.ingestion.orchestration.handler.StreamExtractorHandler;
import com.codeshare.airline.ingestion.orchestration.parsers.MessageParser;
import com.codeshare.airline.ingestion.orchestration.processing.ProcessingStrategy;
import com.codeshare.airline.ingestion.orchestration.processing.ProcessingStrategyFactory;
import com.codeshare.airline.ingestion.persistence.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.ingestion.persistence.dto.ssim.SsimMetaDataDTO;
import com.codeshare.airline.ingestion.persistence.services.error.ErrorPersistenceService;
import com.codeshare.airline.ingestion.validations.model.ValidationMessage;
import com.codeshare.airline.ingestion.validations.model.ValidationResult;
import com.codeshare.airline.ingestion.validations.orchestrator.BusinessValidationOrchestrator;
import com.codeshare.airline.ingestion.validations.orchestrator.StructuralValidationOrchestrator;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenericStreamLoader {

    private final Map<MessageType, StreamExtractorHandler> extractorMap;
    private final Map<MessageType, MessageParser<?>> parserMap;

    private final StructuralValidationOrchestrator structuralValidationOrchestrator;
    private final BusinessValidationOrchestrator businessValidationOrchestrator;
    private final ProcessingStrategyFactory strategyFactory;
    private final ErrorPersistenceService errorService;

    // 🔥 Thread pool (DO NOT shutdown per request)
    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static final int MAX_PARALLEL_TASKS = 200;
    private final Semaphore semaphore = new Semaphore(MAX_PARALLEL_TASKS);

    public boolean processStream(InputStream inputStream, ScheduleFileMetaDataDTO metadata, MessageType type) {

        StreamExtractorHandler extractor = extractorMap.get(type);

        MessageParser<?> parser = parserMap.get(type);

        if (extractor == null || parser == null) {
            throw new IllegalStateException("No extractor/parser for type=" + type);
        }

        AtomicBoolean hasErrors = new AtomicBoolean(false);

        List<CompletableFuture<Void>> futures = new CopyOnWriteArrayList<>();

        long start = System.currentTimeMillis();

        extractor.extract(inputStream, lines -> {

            Runnable task = () -> processSingleBlock(lines, metadata, type, parser, hasErrors);

            if (extractor.isParallelSafe()) {
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }

                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    try {
                        task.run();
                    } finally {
                        semaphore.release();
                    }
                }, executor);

                futures.add(future);

            } else {
                task.run();
            }
        });

        //  wait for all async tasks
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        long duration = System.currentTimeMillis() - start;

        log.info("📊 Stream processed | type={} | durationMs={} | hasErrors={}",
                type, duration, hasErrors.get());

        return hasErrors.get();
    }

    // -----------------------------------
    // 🔥 CORE BLOCK PROCESSING
    // -----------------------------------
    @SuppressWarnings("unchecked")
    private void processSingleBlock( List<String> lines,ScheduleFileMetaDataDTO metadata,MessageType type,MessageParser<?> parser,AtomicBoolean hasErrors) {

        AbstractIngestionContext<?, ?> preContext = buildContext(type, metadata, lines);
        /* ================= 1️⃣ STRUCTURAL VALIDATION ================= */

        ValidationResult structural = structuralValidationOrchestrator.validate(preContext);

        if (structural.hasErrors()) {
            hasErrors.set(true);

            errorService.persist(
                    buildContext(type, metadata, lines),
                    ValidationStage.STRUCTURAL,
                    structural.getMessages()
            );
            return;
        }

        /* ================= 2️⃣ PARSE ================= */

        AbstractIngestionContext<?, ?> context;

        try {
            context = ((MessageParser<AbstractIngestionContext<?, ?>>) parser).parse(lines, metadata);
        } catch (Exception ex) {

            hasErrors.set(true);

            errorService.persist(
                    buildContext(type, metadata, lines),
                    ValidationStage.PARSING,
                    List.of(ValidationMessage.parsingError(ex.getMessage()))
            );
            return;
        }

        /* ================= 3️⃣ BUSINESS VALIDATION ================= */

        ValidationResult business = businessValidationOrchestrator.validate(context);

        if (business.hasErrors()) {
            hasErrors.set(true);

            errorService.persist(
                    context,
                    ValidationStage.VALIDATION,
                    business.getMessages()
            );
            return;
        }

        /* ================= 4️⃣ PROCESS ================= */

        ProcessingStrategy<AbstractIngestionContext<?, ?>> strategy = strategyFactory.get(context);

        if (strategy == null) {
            hasErrors.set(true);

            errorService.persist(
                    context,
                    ValidationStage.PROCESSING,
                    List.of(ValidationMessage.systemError("No processing strategy found"))
            );
            return;
        }

        try {
            strategy.process(context);
        } catch (Exception ex) {
            hasErrors.set(true);

            errorService.persist(
                    context,
                    ValidationStage.PROCESSING,
                    List.of(ValidationMessage.systemError(ex.getMessage()))
            );
        }
    }

    // -----------------------------------
    // CLEAN SHUTDOWN (ONLY HERE)
    // -----------------------------------
    @PreDestroy
    public void shutdown() {
        log.info("Shutting down executor...");
        executor.shutdownNow();
    }

    private AbstractIngestionContext<?, ?> buildContext(
            MessageType type,
            Object metadata,
            List<String> lines
    ) {

        return switch (type) {

            case SSM -> SsmIngestionContext.builder()
                    .messageType(MessageType.SSM)
                    .metadata((ScheduleFileMetaDataDTO) metadata)
                    .messageLines(lines)
                    .build();

            case ASM -> AsmIngestionContext.builder()
                    .messageType(MessageType.ASM)
                    .metadata((ScheduleFileMetaDataDTO) metadata)
                    .messageLines(lines)
                    .build();

            case SSIM -> SsimIngestionContext.builder()
                    .messageType(MessageType.SSIM)
                    .metadata((SsimMetaDataDTO) metadata)
                    .messageLines(lines)
                    .build();
        };
    }


}