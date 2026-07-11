package com.codeshare.airline.schedule.ingestion.orchestration.processor;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.orchestration.pipelines.ScheduleMessageIngestionPipeline;
import com.codeshare.airline.schedule.ingestion.persistence.services.common.ScheduleFileService;
import org.springframework.stereotype.Component;

@Component
public class ScheduleMessageChapterProcessor extends GenericChapterProcessor {

    public ScheduleMessageChapterProcessor(ScheduleMessageIngestionPipeline pipeline,
                                           ScheduleFileService scheduleService) {
        super(pipeline, scheduleService);
    }

    @Override
    protected boolean supports(MessageType type) {
        return type == MessageType.ASM || type == MessageType.SSM;
    }
}
