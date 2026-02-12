package com.codeshare.airline.ssim.test.scheduler;

import com.codeshare.airline.ssim.processor.SsimSourceCoordinator;
import com.codeshare.airline.ssim.source.SsimSourceType;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SsimIngestionScheduler {

    private final SsimSourceCoordinator coordinator;

    @Scheduled(cron = "0 */5 * * * ?")
    public void pollSources() {
        coordinator.ingest(SsimSourceType.LOCAL);
        coordinator.ingest(SsimSourceType.SFTP);
    }
}
