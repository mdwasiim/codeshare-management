package com.codeshare.airline.schedule.orchestration.processor.asm;

import com.codeshare.airline.schedule.domain.common.ProcessingStatus;
import com.codeshare.airline.schedule.domain.common.ScheduleMessageType;
import com.codeshare.airline.schedule.domain.contex.AsmIngestionContext;
import com.codeshare.airline.schedule.orchestration.pipeline.asm.AsmIngestionPipeline;
import com.codeshare.airline.schedule.orchestration.processor.ScheduleChapterProcessor;
import com.codeshare.airline.schedule.parsing.asm.dto.AsmInboundFileDTO;
import com.codeshare.airline.schedule.persistence.asm.service.AsmInboundFileService;
import com.codeshare.airline.schedule.source.ScheduleSourceFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AsmChapterProcessor implements ScheduleChapterProcessor {

    private final AsmInboundFileService inboundFileService;
    private final AsmIngestionPipeline pipeline;

    @Override
    public boolean supports(ScheduleMessageType type) {
        return type == ScheduleMessageType.ASM;
    }

    @Override
    public void process(ScheduleSourceFile sourceFile) {

        AsmInboundFileDTO inboundFile = inboundFileService.create(sourceFile);

        AsmIngestionContext context = AsmIngestionContext.builder()
                .sourceFile(sourceFile)
                .build();

        try {

            pipeline.execute(context);

            inboundFileService.updateStatus(inboundFile.getFileId(), ProcessingStatus.COMPLETED);

        } catch (Exception ex) {

            log.error("ASM processing failed for fileId={}", inboundFile.getFileId(), ex);

            inboundFileService.markFailed(inboundFile.getFileId(), ex);

            throw ex;
        }
    }
}