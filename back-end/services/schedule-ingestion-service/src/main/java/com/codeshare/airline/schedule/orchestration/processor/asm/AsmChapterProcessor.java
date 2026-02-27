package com.codeshare.airline.schedule.orchestration.processor.asm;

import com.codeshare.airline.schedule.domain.common.ProcessingStatus;
import com.codeshare.airline.schedule.domain.common.ScheduleMessageType;
import com.codeshare.airline.schedule.domain.context.AsmIngestionContext;
import com.codeshare.airline.schedule.orchestration.pipeline.asm.AsmIngestionPipeline;
import com.codeshare.airline.schedule.orchestration.processor.ScheduleChapterProcessor;
import com.codeshare.airline.schedule.persistence.inbound.entity.ScheduleInboundFile;
import com.codeshare.airline.schedule.persistence.inbound.service.ScheduleInboundPersistenceService;
import com.codeshare.airline.schedule.source.ScheduleSourceFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AsmChapterProcessor implements ScheduleChapterProcessor {

    private final ScheduleInboundPersistenceService inboundService;
    private final AsmIngestionPipeline pipeline;

    @Override
    public boolean supports(ScheduleMessageType type) {
        return type == ScheduleMessageType.ASM;
    }

    @Override
    @Transactional
    public void process(ScheduleSourceFile sourceFile) {

        ScheduleInboundFile metadata =
                inboundService.createIfNotExists(sourceFile);

        if (metadata.getProcessingStatus() == ProcessingStatus.COMPLETED) {
            return;
        }

        AsmIngestionContext context =
                AsmIngestionContext.builder()
                        .sourceFile(sourceFile)
                        .inboundFile(metadata)
                        .build();

        pipeline.execute(context);
    }
}