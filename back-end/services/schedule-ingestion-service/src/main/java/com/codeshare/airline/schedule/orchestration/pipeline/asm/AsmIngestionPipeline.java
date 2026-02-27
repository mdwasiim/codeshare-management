package com.codeshare.airline.schedule.orchestration.pipeline.asm;

import com.codeshare.airline.schedule.domain.common.ProcessingStatus;
import com.codeshare.airline.schedule.domain.context.AsmIngestionContext;
import com.codeshare.airline.schedule.orchestration.pipeline.AbstractIngestionPipeline;
import com.codeshare.airline.schedule.orchestration.stage.asm.business.AsmBusinessValidationStage;
import com.codeshare.airline.schedule.orchestration.stage.asm.file.AsmFileValidationStage;
import com.codeshare.airline.schedule.orchestration.stage.asm.parsing.AsmParsingStage;
import com.codeshare.airline.schedule.orchestration.stage.asm.structural.AsmStructuralValidation;
import com.codeshare.airline.schedule.persistence.inbound.service.ScheduleInboundPersistenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AsmIngestionPipeline
        extends AbstractIngestionPipeline<AsmIngestionContext> {

    private final AsmFileValidationStage fileStage;
    private final AsmStructuralValidation structuralStage;
    private final AsmParsingStage parsingStage;
    private final AsmBusinessValidationStage businessStage;
    private final ScheduleInboundPersistenceService persistenceService;

    @Override
    protected String getFileId(AsmIngestionContext context) {
        return context.getInboundFile().getFileId();
    }

    @Override
    protected void fileValidation(AsmIngestionContext context) {
        fileStage.execute(context);
    }

    @Override
    protected void structuralValidation(AsmIngestionContext context) {
        structuralStage.execute(context);
    }

    @Override
    protected void parsing(AsmIngestionContext context) {
        parsingStage.execute(context);
    }

    @Override
    protected void businessValidation(AsmIngestionContext context) {
        businessStage.execute(context);
    }

    @Override
    protected boolean hasStructuralBlockingErrors(AsmIngestionContext context) {
        return context.getStructuralResult() != null &&
                context.getStructuralResult().hasBlockingErrors();
    }

    @Override
    protected boolean hasBusinessBlockingErrors(AsmIngestionContext context) {
        return context.getBusinessResult() != null &&
                context.getBusinessResult().hasBlockingErrors();
    }

    @Override
    protected void updateStatus(AsmIngestionContext context,
                                ProcessingStatus status) {

        persistenceService.updateStatus(
                context.getInboundFile(),
                status
        );
    }
}