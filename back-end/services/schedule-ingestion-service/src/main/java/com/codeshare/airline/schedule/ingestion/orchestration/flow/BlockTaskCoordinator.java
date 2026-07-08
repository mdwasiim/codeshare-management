package com.codeshare.airline.schedule.ingestion.orchestration.flow;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

@Slf4j
@Component
public class BlockTaskCoordinator {

    private static final int MAX_PARALLEL_TASKS = 200;

    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private final Semaphore semaphore = new Semaphore(MAX_PARALLEL_TASKS);

    public void execute(boolean parallelSafe, Runnable task, List<CompletableFuture<Void>> futures) {
        if (!parallelSafe) {
            task.run();
            return;
        }

        try {
            semaphore.acquire();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while scheduling ingestion block", ex);
        }

        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try {
                task.run();
            } finally {
                semaphore.release();
            }
        }, executor);

        futures.add(future);
    }

    public List<CompletableFuture<Void>> newFutureList() {
        return new ArrayList<>();
    }

    public void awaitAll(List<CompletableFuture<Void>> futures) {
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    @PreDestroy
    public void shutdown() {
        log.info("Shutting down block task coordinator");
        executor.shutdownNow();
    }
}
