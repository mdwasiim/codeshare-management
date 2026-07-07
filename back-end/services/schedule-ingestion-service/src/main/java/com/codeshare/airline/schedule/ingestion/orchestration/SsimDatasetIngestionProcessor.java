package com.codeshare.airline.schedule.ingestion.orchestration;

import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.enums.ProcessingStatus;
import com.codeshare.airline.schedule.ingestion.orchestration.processor.SsimDatasetChapterProcessor;
import com.codeshare.airline.schedule.ingestion.source.model.ScheduleSourceFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SsimDatasetIngestionProcessor extends ScheduleIngestionProcessor {

    private final SsimDatasetChapterProcessor processor;

    @Override
    protected boolean supports(MessageType type) {
        return type == MessageType.SSIM;
    }

    @Override
    protected ProcessingStatus processSourceFile(ScheduleSourceFile sourceFile) {
        return processor.process(sourceFile);
    }
}
