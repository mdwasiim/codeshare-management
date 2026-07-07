package com.codeshare.airline.schedule.ingestion.orchestration.pipelines;

import com.codeshare.airline.schedule.ingestion.domain.context.AbstractIngestionContext;
import com.codeshare.airline.schedule.ingestion.domain.context.AsmIngestionContext;
import com.codeshare.airline.schedule.ingestion.domain.context.SsimIngestionContext;
import com.codeshare.airline.schedule.ingestion.domain.context.SsmIngestionContext;
import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.enums.ProcessingStatus;
import com.codeshare.airline.schedule.ingestion.domain.enums.ValidationStage;
import com.codeshare.airline.schedule.ingestion.orchestration.loader.GenericStreamLoader;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.schedule.ingestion.dto.ssim.SsimMetaDataDTO;
import com.codeshare.airline.schedule.ingestion.persistence.services.common.ScheduleFileService;
import com.codeshare.airline.schedule.ingestion.persistence.services.error.ErrorPersistenceService;
import com.codeshare.airline.schedule.ingestion.source.model.ScheduleSourceFile;
import com.codeshare.airline.schedule.ingestion.validation.model.ValidationResult;
import com.codeshare.airline.schedule.ingestion.validation.orchestrator.FileValidatorOrchestrator;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

@Slf4j
public abstract class GenericIngestionPipeline {

    protected final GenericStreamLoader genericStreamLoader;
    protected final ScheduleFileService scheduleService;
    protected final FileValidatorOrchestrator fileValidatorOrchestrator;
    protected final ErrorPersistenceService errorPersistenceService;

    protected GenericIngestionPipeline(GenericStreamLoader genericStreamLoader,
                                       ScheduleFileService scheduleService,
                                       FileValidatorOrchestrator fileValidatorOrchestrator,
                                       ErrorPersistenceService errorPersistenceService) {
        this.genericStreamLoader = genericStreamLoader;
        this.scheduleService = scheduleService;
        this.fileValidatorOrchestrator = fileValidatorOrchestrator;
        this.errorPersistenceService = errorPersistenceService;
    }

    public ProcessingStatus execute(ScheduleSourceFile scheduleSourceFile, ScheduleFileMetaDataDTO metadata, MessageType type) {

        if (!supports(type)) {
            throw new IllegalStateException("Unsupported message type " + type);
        }

        log.info("Starting pipeline | fileId={} | type={}", metadata.getId(), type);

        try {
            // =========================
            // 1. VALIDATION
            // =========================
            updateStatus(metadata, ProcessingStatus.VALIDATING);

            ValidationResult result = fileValidatorOrchestrator.validate(type, metadata);

            if (result.hasErrors()) {
                errorPersistenceService.persist(
                        buildFileContext(metadata, type),
                        ValidationStage.FILE_TYPE,
                        result.getMessages()
                );

                return updateStatus(metadata, ProcessingStatus.FAILED);
            }

            // =========================
            // 2. PROCESSING
            // =========================
            updateStatus(metadata, ProcessingStatus.PROCESSING);

            long start = System.currentTimeMillis();

            boolean hasErrors = scheduleSourceFile.consumeStream(
                    is -> processStream(is, metadata, type)
            );

            long duration = System.currentTimeMillis() - start;

            log.info("Processing done | fileId={} | durationMs={} | hasErrors={}", metadata.getId(), duration, hasErrors);

            // =========================
            // 3. FINAL STATUS
            // =========================
            if (hasErrors) {
                return updateStatus(metadata, ProcessingStatus.PARTIAL);
            }

            return updateStatus(metadata, ProcessingStatus.COMPLETED);

        } catch (Exception ex) {

            updateStatus(metadata, ProcessingStatus.FAILED);
            log.error("Pipeline failed | fileId={} | type={}", metadata.getId(), type, ex);
            throw ex;
        }
    }

    /**
     * Stream processing extension point.
     */
    protected boolean processStream(InputStream is, ScheduleFileMetaDataDTO metadata, MessageType type) {
        return genericStreamLoader.processStream(is, metadata, type);
    }

    protected abstract boolean supports(MessageType type);

    private ProcessingStatus updateStatus(ScheduleFileMetaDataDTO metadata, ProcessingStatus status) {
        if (metadata.getProcessingStatus() != status) {
            scheduleService.updateScheduleStatus(metadata, status);
        }
        return metadata.getProcessingStatus();
    }

    private AbstractIngestionContext<?, ?> buildFileContext(ScheduleFileMetaDataDTO metadata, MessageType type) {

        if (MessageType.ASM.equals(type)) {
            return AsmIngestionContext.builder()
                    .messageType(MessageType.ASM)
                    .metadata(metadata)
                    .build();
        }
        if (MessageType.SSM.equals(type)) {
            return SsmIngestionContext.builder()
                    .messageType(MessageType.SSM)
                    .metadata(metadata)
                    .build();
        }
        if (metadata instanceof SsimMetaDataDTO dto) {
            return SsimIngestionContext.builder()
                    .messageType(MessageType.SSIM)
                    .metadata(dto)
                    .build();
        }

        log.error(" Unknown metadata type: {}", metadata.getClass());
        throw new IllegalStateException("Unknown metadata type");
    }
}
