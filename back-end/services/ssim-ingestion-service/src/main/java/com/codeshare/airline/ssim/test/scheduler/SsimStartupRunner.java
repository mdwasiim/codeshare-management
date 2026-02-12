package com.codeshare.airline.ssim.test.scheduler;

import com.codeshare.airline.ssim.processor.SsimSourceCoordinator;
import com.codeshare.airline.ssim.source.SsimSourceType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SsimStartupRunner {

    private final SsimSourceCoordinator coordinator;

    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() {
        coordinator.ingest(SsimSourceType.LOCAL);
    }
}
