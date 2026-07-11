package com.codeshare.airline.identity.access.data;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader {

    private final IdentityTenantBootstrapService bootstrapService;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("Identity service started. Ensuring bootstrap data...");
        init();
    }

    @Scheduled(fixedDelay = 30000, initialDelay = 60000)
    public void retryIfNeeded() {
        if (!isInitialized()) {
            log.warn("Initialization incomplete. Retrying...");
            init();
        }
    }

    private synchronized void init() {
        try {
            log.info("Starting identity data initialization");
            bootstrapService.bootstrapAllTenants();
            log.info("Identity data initialization completed");
        } catch (Exception ex) {
            log.error("Initialization failed. Will retry.", ex);
        }
    }

    private boolean isInitialized() {
        return bootstrapService.isInitialized();
    }
}
