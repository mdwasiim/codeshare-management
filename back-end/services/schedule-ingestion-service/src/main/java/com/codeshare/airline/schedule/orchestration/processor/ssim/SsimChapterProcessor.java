package com.codeshare.airline.schedule.orchestration.processor.ssim;

import com.codeshare.airline.schedule.domain.common.ProcessingStatus;
import com.codeshare.airline.schedule.domain.common.ScheduleMessageType;
import com.codeshare.airline.schedule.domain.contex.SsimIngestionContext;
import com.codeshare.airline.schedule.orchestration.pipeline.ssim.SsimIngestionPipeline;
import com.codeshare.airline.schedule.orchestration.processor.ScheduleChapterProcessor;
import com.codeshare.airline.schedule.parsing.ssim.dto.SsimInboundFileDTO;
import com.codeshare.airline.schedule.persistence.ssim.service.SsimInboundFileService;
import com.codeshare.airline.schedule.source.ScheduleSourceFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SsimChapterProcessor implements ScheduleChapterProcessor {

    private final SsimInboundFileService inboundFileService;
    private final SsimIngestionPipeline pipeline;

    @Override
    public boolean supports(ScheduleMessageType type) {
        return type == ScheduleMessageType.SSIM;
    }

    @Override
    public void process(ScheduleSourceFile sourceFile) {

        SsimInboundFileDTO inboundFile = inboundFileService.create(sourceFile);

        SsimIngestionContext context = SsimIngestionContext.builder()
                .sourceFile(sourceFile)
                .build();
        try {
            pipeline.execute(context);

            inboundFileService.updateStatus(inboundFile.getFileId(), ProcessingStatus.COMPLETED);
        } catch (Exception ex) {
            log.error("SSIM failed for fileId={}", inboundFile.getFileId(), ex);
            inboundFileService.markFailed(inboundFile.getFileId(), ex);
        }
    }
}

