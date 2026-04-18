package com.codeshare.airline.inbound.orchestration.processor;

import com.codeshare.airline.inbound.source.inbound.ScheduleSourceFile;

public interface ScheduleChapterProcessor {

    void process(ScheduleSourceFile sourceFile);
}