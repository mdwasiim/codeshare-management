package com.codeshare.airline.schedule.ingestion.orchestration.processor;

import com.codeshare.airline.schedule.ingestion.source.model.ScheduleSourceFile;
import com.codeshare.airline.schedule.ingestion.domain.enums.ProcessingStatus;

public interface ScheduleChapterProcessor {

    ProcessingStatus process(ScheduleSourceFile sourceFile);
}
