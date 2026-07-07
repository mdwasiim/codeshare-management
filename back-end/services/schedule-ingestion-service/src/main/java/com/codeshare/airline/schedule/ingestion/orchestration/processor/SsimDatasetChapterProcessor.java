package com.codeshare.airline.schedule.ingestion.orchestration.processor;

import com.codeshare.airline.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.orchestration.pipelines.SsimDatasetIngestionPipeline;
import com.codeshare.airline.schedule.ingestion.persistence.services.common.ScheduleFileService;
import org.springframework.stereotype.Component;

@Component
public class SsimDatasetChapterProcessor extends GenericChapterProcessor {

    public SsimDatasetChapterProcessor(SsimDatasetIngestionPipeline pipeline,
                                       ScheduleFileService scheduleService) {
        super(pipeline, scheduleService);
    }

    @Override
    protected boolean supports(MessageType type) {
        return type == MessageType.SSIM;
    }
}
