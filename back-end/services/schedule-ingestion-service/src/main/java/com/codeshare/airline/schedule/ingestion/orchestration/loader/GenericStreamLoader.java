package com.codeshare.airline.schedule.ingestion.orchestration.loader;

import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.context.AbstractIngestionContext;
import com.codeshare.airline.schedule.ingestion.domain.context.AsmIngestionContext;
import com.codeshare.airline.schedule.ingestion.domain.context.SsimIngestionContext;
import com.codeshare.airline.schedule.ingestion.domain.context.SsmIngestionContext;
import com.codeshare.airline.schedule.ingestion.domain.enums.ValidationStage;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.schedule.ingestion.dto.ssim.SsimMetaDataDTO;
import com.codeshare.airline.schedule.ingestion.orchestration.handler.StreamExtractorHandler;
import com.codeshare.airline.schedule.ingestion.orchestration.parsers.MessageParser;
import com.codeshare.airline.schedule.ingestion.orchestration.processing.ProcessingStrategy;
import com.codeshare.airline.schedule.ingestion.orchestration.processing.ProcessingStrategyFactory;
import com.codeshare.airline.schedule.ingestion.persistence.services.error.ErrorPersistenceService;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationMessage;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationResult;
import com.codeshare.airline.schedule.ingestion.validation.orchestrator.BusinessValidationOrchestrator;
import com.codeshare.airline.schedule.ingestion.validation.orchestrator.StructuralValidationOrchestrator;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public abstract class GenericStreamLoader {

    private static final int MAX_PARALLEL_TASKS = 200;

    private final Map<MessageType, StreamExtractorHandler> extractorMap;
    private final Map<MessageType, MessageParser<?>> parserMap;
    private final StructuralValidationOrchestrator structuralValidationOrchestrator;
    private final BusinessValidationOrchestrator businessValidationOrchestrator;
    private final ProcessingStrategyFactory strategyFactory;
    private final ErrorPersistenceService errorService;
    private final ExecutorService executor =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private final Semaphore semaphore = new Semaphore(MAX_PARALLEL_TASKS);

    protected GenericStreamLoader(Map<MessageType, StreamExtractorHandler> extractorMap,
                                  Map<MessageType, MessageParser<?>> parserMap,
                                  StructuralValidationOrchestrator structuralValidationOrchestrator,
                                  BusinessValidationOrchestrator businessValidationOrchestrator,
                                  ProcessingStrategyFactory strategyFactory,
                                  ErrorPersistenceService errorService) {
        this.extractorMap = extractorMap;
        this.parserMap = parserMap;
        this.structuralValidationOrchestrator = structuralValidationOrchestrator;
        this.businessValidationOrchestrator = businessValidationOrchestrator;
        this.strategyFactory = strategyFactory;
        this.errorService = errorService;
    }

    public boolean processStream(InputStream inputStream, ScheduleFileMetaDataDTO metadata, MessageType type) {

        if (!supports(type)) {
            throw new IllegalStateException("Unsupported message type " + type);
        }

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
                    hasErrors.set(true);
                    throw new IllegalStateException("Interrupted while scheduling " + type + " block", e);
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

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        long duration = System.currentTimeMillis() - start;
        log.info("Stream processed | type={} | durationMs={} | hasErrors={}",
                type, duration, hasErrors.get());

        return hasErrors.get();
    }

    @SuppressWarnings("unchecked")
    private void processSingleBlock(List<String> lines,
                                    ScheduleFileMetaDataDTO metadata,
                                    MessageType type,
                                    MessageParser<?> parser,
                                    AtomicBoolean hasErrors) {

        AbstractIngestionContext<?, ?> preContext = buildContext(type, metadata, lines);
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

        ValidationResult business = businessValidationOrchestrator.validate(context);
        if (business.hasErrors()) {
            hasErrors.set(true);
            errorService.persist(context, ValidationStage.VALIDATION, business.getMessages());
            return;
        }

        ProcessingStrategy<AbstractIngestionContext<?, ?>> strategy;
        try {
            strategy = strategyFactory.get(context);
        } catch (Exception ex) {
            hasErrors.set(true);
            errorService.persist(
                    context,
                    ValidationStage.PROCESSING,
                    List.of(ValidationMessage.systemError(ex.getMessage()))
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

    @PreDestroy
    public void shutdown() {
        log.info("Shutting down executor...");
        executor.shutdownNow();
    }

    private AbstractIngestionContext<?, ?> buildContext(MessageType type, Object metadata, List<String> lines) {
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
            case SSIM -> {
                if (!(metadata instanceof SsimMetaDataDTO dto)) {
                    throw new IllegalArgumentException("Invalid metadata for SSIM context: "
                            + (metadata == null ? "null" : metadata.getClass().getName()));
                }
                yield SsimIngestionContext.builder()
                        .messageType(MessageType.SSIM)
                        .metadata(dto)
                        .messageLines(lines)
                        .build();
            }
        };
    }

    protected abstract boolean supports(MessageType type);
}
