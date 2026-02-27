package com.codeshare.airline.schedule.orchestration.pipeline;

import com.codeshare.airline.schedule.domain.common.ProcessingStatus;
import com.codeshare.airline.schedule.domain.contex.AbstractIngestionContext;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public abstract class AbstractIngestionPipeline<
        TContext extends AbstractIngestionContext<?, ?>> {

    public final void execute(TContext context) {

        String fileId = getFileId(context);

        log.info("FILE stage for fileId={}", fileId);
        fileValidation(context);

        log.info("STRUCTURAL stage for fileId={}", fileId);
        structuralValidation(context);

        if (hasStructuralBlockingErrors(context)) {
            updateStatus(context, ProcessingStatus.STRUCTURAL_FAILED);
            return;
        }

        updateStatus(context, ProcessingStatus.PARSING);

        log.info("PARSING stage for fileId={}", fileId);
        parsing(context);

        updateStatus(context, ProcessingStatus.BUSINESS_VALIDATING);

        log.info("BUSINESS stage for fileId={}", fileId);
        businessValidation(context);

        if (hasBusinessBlockingErrors(context)) {
            updateStatus(context, ProcessingStatus.BUSINESS_FAILED);
            return;
        }

        updateStatus(context, ProcessingStatus.COMPLETED);
    }

    protected abstract String getFileId(TContext context);

    protected abstract void fileValidation(TContext context);

    protected abstract void structuralValidation(TContext context);

    protected abstract void parsing(TContext context);

    protected abstract void businessValidation(TContext context);

    protected abstract boolean hasStructuralBlockingErrors(TContext context);

    protected abstract boolean hasBusinessBlockingErrors(TContext context);

    protected abstract void updateStatus(TContext context, ProcessingStatus status);
}