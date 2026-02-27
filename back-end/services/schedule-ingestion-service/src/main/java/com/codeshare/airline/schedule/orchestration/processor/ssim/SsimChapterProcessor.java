package com.codeshare.airline.schedule.orchestration.processor.ssim;

import com.codeshare.airline.schedule.domain.common.ProcessingStatus;
import com.codeshare.airline.schedule.domain.common.ScheduleMessageType;
import com.codeshare.airline.schedule.domain.contex.SsimIngestionContext;
import com.codeshare.airline.schedule.orchestration.pipeline.ssim.SsimIngestionPipeline;
import com.codeshare.airline.schedule.orchestration.processor.ScheduleChapterProcessor;
import com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundFile;
import com.codeshare.airline.schedule.persistence.ssim.service.SsimInboundFileService;
import com.codeshare.airline.schedule.source.ScheduleSourceFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class SsimChapterProcessor implements ScheduleChapterProcessor {

    private final SsimInboundFileService inboundService;
    private final SsimIngestionPipeline pipeline;

    @Override
    public boolean supports(ScheduleMessageType type) {
        return type == ScheduleMessageType.SSIM;
    }

    @Override
    @Transactional
    public void process(ScheduleSourceFile sourceFile) {

        SsimInboundFile metadata = inboundService.create(sourceFile);

        if (metadata.getProcessingStatus() == ProcessingStatus.COMPLETED) {
            return;
        }

        SsimIngestionContext context =
                SsimIngestionContext.builder()
                        .sourceFile(sourceFile)
                        .inboundFile(metadata)
                        .build();

        pipeline.execute(context);
    }
}