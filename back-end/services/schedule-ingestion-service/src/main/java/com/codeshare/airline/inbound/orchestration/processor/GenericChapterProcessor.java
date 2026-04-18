package com.codeshare.airline.inbound.orchestration.processor;

import com.codeshare.airline.enums.MessageType;
import com.codeshare.airline.inbound.orchestration.pipelines.GenericIngestionPipeline;
import com.codeshare.airline.inbound.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.inbound.services.common.ScheduleFileService;
import com.codeshare.airline.inbound.source.inbound.ScheduleSourceFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenericChapterProcessor implements ScheduleChapterProcessor {

    private final GenericIngestionPipeline pipeline;
    private final ScheduleFileService scheduleService;

    @Override
    public void process(ScheduleSourceFile scheduleSourceFile) {

        MessageType type = scheduleSourceFile.getMessageType();

        if (type == null) {
            throw new IllegalStateException("MessageType missing for file=" + scheduleSourceFile.getFileName());
        }

        if (pipeline == null) {
            throw new IllegalStateException("No pipeline configured for type=" + type);
        }

        log.info(" Starting ingestion | file={} type={} fileId={}", scheduleSourceFile.getFileName(), type, scheduleSourceFile.getFileId());

        ScheduleFileMetaDataDTO metadata = scheduleService.createInbound(scheduleSourceFile, type);

        if (metadata == null) {
            throw new IllegalStateException("Metadata creation failed for file=" + scheduleSourceFile.getFileName());
        }

        try {

            pipeline.execute(scheduleSourceFile, metadata, type);

            log.info(" Ingestion completed | file={} type={}", scheduleSourceFile.getFileName(), type);

        } catch (Exception ex) {
            log.error(" Ingestion failed | file={} type={}", scheduleSourceFile.getFileName(), type, ex);
            throw ex;
        }
    }
}