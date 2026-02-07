package com.codeshare.airline.ingestion.scheduler;

import com.codeshare.airline.ingestion.model.SsimRawFile;
import com.codeshare.airline.ingestion.parsing.SsimParsingPipeline;
import com.codeshare.airline.ingestion.source.SsimSourceAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SsimIngestionScheduler {

    private final List<SsimSourceAdapter> adapters;
    private final SsimParsingPipeline pipeline;

    @Scheduled(fixedDelayString = "${ssim.poll.interval}")
    public void poll() {

        for (SsimSourceAdapter adapter : adapters) {

            if (!adapter.isEnabled()) continue;

            for (SsimRawFile file : adapter.poll()) {
                try {
                    pipeline.process(file);
                    adapter.onSuccess(file);
                } catch (Exception e) {
                    log.error("SSIM ingestion failed", e);
                    adapter.onFailure(file, e);
                }
            }
        }
    }
}

