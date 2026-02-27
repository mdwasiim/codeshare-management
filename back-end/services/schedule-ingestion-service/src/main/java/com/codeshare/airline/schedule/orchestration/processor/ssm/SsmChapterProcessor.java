package com.codeshare.airline.schedule.orchestration.processor.ssm;

import com.codeshare.airline.schedule.domain.common.ProcessingStatus;
import com.codeshare.airline.schedule.domain.common.ScheduleMessageType;
import com.codeshare.airline.schedule.domain.contex.SsmIngestionContext;
import com.codeshare.airline.schedule.orchestration.pipeline.ssm.SsmIngestionPipeline;
import com.codeshare.airline.schedule.orchestration.processor.ScheduleChapterProcessor;
import com.codeshare.airline.schedule.persistence.inbound.entity.ScheduleInboundFile;
import com.codeshare.airline.schedule.persistence.inbound.service.ScheduleInboundPersistenceService;
import com.codeshare.airline.schedule.source.ScheduleSourceFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SsmChapterProcessor implements ScheduleChapterProcessor {

    private final ScheduleInboundPersistenceService inboundService;
    private final SsmIngestionPipeline pipeline;

    @Override
    public boolean supports(ScheduleMessageType type) {
        return type == ScheduleMessageType.SSM;
    }

    @Override
    @Transactional
    public void process(ScheduleSourceFile sourceFile) {

        ScheduleInboundFile metadata =
                inboundService.createIfNotExists(sourceFile);

        if (metadata.getProcessingStatus() == ProcessingStatus.COMPLETED) {
            return;
        }

        SsmIngestionContext context =
                SsmIngestionContext.builder()
                        .sourceFile(sourceFile)
                        .inboundFile(metadata)
                        .build();

        pipeline.execute(context);
    }
}