package com.codeshare.airline.schedule.orchestration.processor;

import com.codeshare.airline.schedule.domain.common.ScheduleMessageType;
import com.codeshare.airline.schedule.source.ScheduleSourceFile;

public interface ScheduleChapterProcessor {

    boolean supports(ScheduleMessageType type);

    void process(ScheduleSourceFile sourceFile);
}