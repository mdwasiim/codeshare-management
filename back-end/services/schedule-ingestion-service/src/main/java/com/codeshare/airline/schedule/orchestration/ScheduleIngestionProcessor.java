package com.codeshare.airline.schedule.orchestration;

import com.codeshare.airline.schedule.source.ScheduleSourceFile;

public interface ScheduleIngestionProcessor {

    /**
     * Process a single SSIM file using streaming.
     *
     * This method MUST NOT load the full file into memory.
     */
    void process(ScheduleSourceFile sourceFile);
}
