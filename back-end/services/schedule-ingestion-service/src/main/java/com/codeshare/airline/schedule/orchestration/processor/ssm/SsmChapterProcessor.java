package com.codeshare.airline.schedule.orchestration.processor.ssm;

import com.codeshare.airline.schedule.domain.common.ProcessingStatus;
import com.codeshare.airline.schedule.domain.common.ScheduleMessageType;
import com.codeshare.airline.schedule.domain.contex.SsmIngestionContext;
import com.codeshare.airline.schedule.orchestration.pipeline.ssm.SsmIngestionPipeline;
import com.codeshare.airline.schedule.orchestration.processor.ScheduleChapterProcessor;
import com.codeshare.airline.schedule.persistence.ssm.service.SsmInboundFileService;
import com.codeshare.airline.schedule.source.ScheduleSourceFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SsmChapterProcessor implements ScheduleChapterProcessor {

    private final SsmInboundFileService inboundFileService;
    private final SsmIngestionPipeline pipeline;

    @Override
    public boolean supports(ScheduleMessageType type) {
        return type == ScheduleMessageType.SSM;
    }

    @Override
    public void process(ScheduleSourceFile sourceFile) {

        var inboundFile = inboundFileService.create(sourceFile);

        SsmIngestionContext context = SsmIngestionContext.builder()
                .sourceFile(sourceFile)
                .build();

        try {

            pipeline.execute(context);

            inboundFileService.updateStatus(inboundFile.getFileId(), ProcessingStatus.COMPLETED);

        } catch (Exception ex) {

            log.error("SSM processing failed for fileId={}", inboundFile.getFileId(), ex);

            inboundFileService.markFailed(inboundFile.getFileId(), ex);

            throw ex;
        }
    }
}