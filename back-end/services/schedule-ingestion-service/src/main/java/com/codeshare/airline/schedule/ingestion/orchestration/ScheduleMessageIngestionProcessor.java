package com.codeshare.airline.schedule.ingestion.orchestration;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.enums.ProcessingStatus;
import com.codeshare.airline.schedule.ingestion.orchestration.processor.ScheduleMessageChapterProcessor;
import com.codeshare.airline.schedule.ingestion.source.model.ScheduleSourceFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduleMessageIngestionProcessor extends ScheduleIngestionProcessor {

    private final ScheduleMessageChapterProcessor processor;

    @Override
    protected boolean supports(MessageType type) {
        return type == MessageType.ASM || type == MessageType.SSM;
    }

    @Override
    protected ProcessingStatus processSourceFile(ScheduleSourceFile sourceFile) {
        return processor.process(sourceFile);
    }
}
