package com.codeshare.airline.inbound.orchestration.pipelines;

import com.codeshare.airline.inbound.domain.context.AbstractIngestionContext;
import com.codeshare.airline.inbound.domain.context.AsmIngestionContext;
import com.codeshare.airline.inbound.domain.context.SsimIngestionContext;
import com.codeshare.airline.inbound.domain.context.SsmIngestionContext;
import com.codeshare.airline.enums.MessageType;
import com.codeshare.airline.inbound.domain.enums.ProcessingStatus;
import com.codeshare.airline.inbound.domain.enums.ValidationStage;
import com.codeshare.airline.inbound.orchestration.loader.GenericStreamLoader;
import com.codeshare.airline.inbound.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.inbound.dto.ssim.SsimMetaDataDTO;
import com.codeshare.airline.inbound.services.common.ScheduleFileService;
import com.codeshare.airline.inbound.services.error.ErrorPersistenceService;
import com.codeshare.airline.inbound.source.inbound.ScheduleSourceFile;
import com.codeshare.airline.inbound.validations.model.ValidationResult;
import com.codeshare.airline.inbound.validations.orchestrator.FileValidatorOrchestrator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenericIngestionPipeline {

    protected final GenericStreamLoader genericStreamLoader;
    protected final ScheduleFileService scheduleService;
    protected final FileValidatorOrchestrator fileValidatorOrchestrator;
    protected final ErrorPersistenceService errorPersistenceService;

    public void execute(ScheduleSourceFile scheduleSourceFile, ScheduleFileMetaDataDTO metadata, MessageType type) {

        log.info(" Starting ingestion | fileId={} | type={}", metadata.getId(), type);

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

                updateStatus(metadata, ProcessingStatus.FAILED);
                return;
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

            log.info("📊 Processing done | fileId={} | durationMs={} | hasErrors={}",  metadata.getId(), duration, hasErrors);

            // =========================
            // 3. FINAL STATUS
            // =========================
            if (hasErrors) {
                updateStatus(metadata, ProcessingStatus.PARTIAL);
            } else {
                updateStatus(metadata, ProcessingStatus.COMPLETED);
            }

        } catch (Exception ex) {

            updateStatus(metadata, ProcessingStatus.FAILED);
            log.error(" Pipeline failed | fileId={} | type={}", metadata.getId(), type, ex);
            throw ex;
        }
    }

    /**
     * 🔥 KEY EXTENSION POINT
     */
    protected boolean processStream(InputStream is, ScheduleFileMetaDataDTO metadata, MessageType type) {
        return genericStreamLoader.processStream(is, metadata, type);
    }

    private void updateStatus(ScheduleFileMetaDataDTO metadata, ProcessingStatus status) {
        if (metadata.getProcessingStatus() != status) {
            scheduleService.updateScheduleStatus(metadata, status);
        }
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