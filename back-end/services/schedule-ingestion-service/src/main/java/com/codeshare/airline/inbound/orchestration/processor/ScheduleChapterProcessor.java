package com.codeshare.airline.inbound.orchestration.processor;

import com.codeshare.airline.inbound.source.inbound.ScheduleSourceFile;
import com.codeshare.airline.inbound.domain.enums.ProcessingStatus;

public interface ScheduleChapterProcessor {

    ProcessingStatus process(ScheduleSourceFile sourceFile);
}
