package com.codeshare.airline.schedule.orchestration.pipeline;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractIngestionPipeline<TContext, TStatus> {

    public final void execute(TContext context) {

        String fileId = getFileId(context);

        log.info("FILE stage for fileId={}", fileId);
        fileValidation(context);

        log.info("STRUCTURAL stage for fileId={}", fileId);
        structuralValidation(context);

        if (hasStructuralBlockingErrors(context)) {
            return;
        }

        updateStatus(context, parsingStatus());

        log.info("PARSING stage for fileId={}", fileId);
        parsing(context);

        updateStatus(context, businessValidatingStatus());

        log.info("BUSINESS stage for fileId={}", fileId);
        businessValidation(context);

        if (hasBusinessBlockingErrors(context)) {
            return;
        }

        updateStatus(context, completedStatus());
    }

    protected abstract String getFileId(TContext context);

    protected abstract void fileValidation(TContext context);

    protected abstract void structuralValidation(TContext context);

    protected abstract void parsing(TContext context);

    protected abstract void businessValidation(TContext context);

    protected abstract boolean hasStructuralBlockingErrors(TContext context);

    protected abstract boolean hasBusinessBlockingErrors(TContext context);

    protected abstract void updateStatus(TContext context, TStatus status);

    protected abstract TStatus parsingStatus();

    protected abstract TStatus businessValidatingStatus();

    protected abstract TStatus completedStatus();
}