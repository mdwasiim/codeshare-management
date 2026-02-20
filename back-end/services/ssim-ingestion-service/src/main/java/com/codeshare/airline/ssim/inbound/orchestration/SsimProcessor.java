package com.codeshare.airline.ssim.inbound.orchestration;

import com.codeshare.airline.ssim.inbound.source.SsimSourceFile;

public interface SsimProcessor {

    /**
     * Process a single SSIM file using streaming.
     *
     * This method MUST NOT load the full file into memory.
     */
    void process(SsimSourceFile sourceFile);
}
