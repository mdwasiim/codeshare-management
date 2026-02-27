package com.codeshare.airline.schedule.orchestration;

import com.codeshare.airline.schedule.source.ScheduleSourceFile;

public interface ScheduleIngestionProcessor {

    /**
     * Process inbound schedule file.
     *
     * Implementation may buffer content in memory if required
     * for validation and multi-stage parsing.
     */
    void process(ScheduleSourceFile sourceFile);
}
