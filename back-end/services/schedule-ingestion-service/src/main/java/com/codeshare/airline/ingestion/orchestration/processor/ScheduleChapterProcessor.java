package com.codeshare.airline.ingestion.orchestration.processor;

import com.codeshare.airline.ingestion.source.inbound.ScheduleSourceFile;

public interface ScheduleChapterProcessor {

    void process(ScheduleSourceFile sourceFile);
}