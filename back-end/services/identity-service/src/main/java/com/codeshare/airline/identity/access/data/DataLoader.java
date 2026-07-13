package com.codeshare.airline.identity.access.data;

import feign.RetryableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.ConnectException;

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
        try {
            if (!bootstrapService.isInitialized()) {
                log.warn("Initialization incomplete. Retrying...");
                init();
            }
        } catch (Exception ex) {
            if (isDependencyUnavailable(ex)) {
                log.warn("Identity bootstrap deferred. Tenant service is not reachable yet. A retry will be attempted.");
                return;
            }

            log.error("Unable to determine identity bootstrap status. Will retry.", ex);
        }
    }

    private synchronized void init() {
        try {
            log.info("Starting identity data initialization");
            bootstrapService.bootstrapAllTenants();
            log.info("Identity data initialization completed");
        } catch (Exception ex) {
            if (isDependencyUnavailable(ex)) {
                log.warn("Identity bootstrap deferred. Tenant service is not reachable yet. A retry will be attempted.");
                return;
            }

            log.error("Initialization failed. Will retry.", ex);
        }
    }

    private boolean isDependencyUnavailable(Throwable throwable) {
        Throwable current = throwable;
        while (current != null) {
            if (current instanceof RetryableException || current instanceof ConnectException) {
                return true;
            }
            current = current.getCause();
        }
        return false;
    }
}
