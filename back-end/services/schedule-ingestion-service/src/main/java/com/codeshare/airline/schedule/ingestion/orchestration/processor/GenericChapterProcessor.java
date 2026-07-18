package com.codeshare.airline.schedule.ingestion.orchestration.processor;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.enums.ProcessingStatus;
import com.codeshare.airline.schedule.ingestion.integration.kafka.ImportCompletedEventPublisher;
import com.codeshare.airline.schedule.ingestion.integration.kafka.ProcessingRequestedEventPublisher;
import com.codeshare.airline.schedule.ingestion.orchestration.pipelines.GenericIngestionPipeline;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.schedule.ingestion.persistence.services.common.ScheduleFileService;
import com.codeshare.airline.schedule.ingestion.shared.exceptions.BusinessValidationException;
import com.codeshare.airline.schedule.ingestion.source.model.ScheduleSourceFile;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class GenericChapterProcessor implements ScheduleChapterProcessor {

    private final GenericIngestionPipeline pipeline;
    private final ScheduleFileService scheduleService;
    private final ImportCompletedEventPublisher importCompletedEventPublisher;
    private final ProcessingRequestedEventPublisher processingRequestedEventPublisher;

    protected GenericChapterProcessor(GenericIngestionPipeline pipeline,
                                      ScheduleFileService scheduleService,
                                      ImportCompletedEventPublisher importCompletedEventPublisher,
                                      ProcessingRequestedEventPublisher processingRequestedEventPublisher) {
        this.pipeline = pipeline;
        this.scheduleService = scheduleService;
        this.importCompletedEventPublisher = importCompletedEventPublisher;
        this.processingRequestedEventPublisher = processingRequestedEventPublisher;
    }

    @Override
    public ProcessingStatus process(ScheduleSourceFile scheduleSourceFile) {

        MessageType type = scheduleSourceFile.getMessageType();

        if (type == null) {
            throw new IllegalStateException("MessageType missing for file=" + scheduleSourceFile.getFileName());
        }

        if (!supports(type)) {
            throw new IllegalStateException("Unsupported message type " + type);
        }

        if (pipeline == null) {
            throw new IllegalStateException("No pipeline configured for type=" + type);
        }

        log.info("Starting ingestion | file={} type={} fileId={}",
                scheduleSourceFile.getFileName(), type, scheduleSourceFile.getFileId());

        ScheduleFileMetaDataDTO metadata = scheduleService.createInbound(scheduleSourceFile, type);

        if (metadata == null) {
            throw new IllegalStateException("Metadata creation failed for file=" + scheduleSourceFile.getFileName());
        }

        if (isAlreadyProcessed(metadata.getProcessingStatus())) {
            log.info(" Skipping already processed file={} type={} fileId={} checksum={}",
                    scheduleSourceFile.getFileName(),
                    type,
                    metadata.getFileId(),
                    metadata.getChecksum());
            return metadata.getProcessingStatus();
        }

        try {

            ProcessingStatus finalStatus = pipeline.execute(scheduleSourceFile, metadata, type);

            if (isFailedOrPartial(finalStatus)) {
                throw new BusinessValidationException(
                        "Ingestion completed with errors for file="
                                + scheduleSourceFile.getFileName()
                                + " status="
                                + finalStatus
                );
            }

            if (shouldPublish(finalStatus)) {
                processingRequestedEventPublisher.publish(metadata);
                importCompletedEventPublisher.publish(metadata);
            }

            log.info("Ingestion completed | file={} type={} status={}",
                    scheduleSourceFile.getFileName(), type, finalStatus);
            return finalStatus;

        } catch (Exception ex) {
            log.error("Ingestion failed | file={} type={}",
                    scheduleSourceFile.getFileName(), type, ex);
            throw ex;
        }
    }

    protected abstract boolean supports(MessageType type);

    private boolean isAlreadyProcessed(ProcessingStatus status) {
        return status == ProcessingStatus.COMPLETED || status == ProcessingStatus.SUCCESS;
    }

    private boolean shouldPublish(ProcessingStatus status) {
        return status == ProcessingStatus.COMPLETED
                || status == ProcessingStatus.SUCCESS;
    }

    private boolean isFailedOrPartial(ProcessingStatus status) {
        return status == ProcessingStatus.FAILED || status == ProcessingStatus.PARTIAL;
    }
}
