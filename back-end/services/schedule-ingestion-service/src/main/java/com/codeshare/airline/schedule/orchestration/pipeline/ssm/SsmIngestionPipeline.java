package com.codeshare.airline.schedule.orchestration.pipeline.ssm;

import com.codeshare.airline.schedule.domain.common.ProcessingStatus;
import com.codeshare.airline.schedule.domain.contex.SsmIngestionContext;
import com.codeshare.airline.schedule.orchestration.pipeline.AbstractIngestionPipeline;
import com.codeshare.airline.schedule.orchestration.stage.ssm.SsmStructuralValidationStage;
import com.codeshare.airline.schedule.orchestration.stage.ssm.business.SsmBusinessValidationStage;
import com.codeshare.airline.schedule.orchestration.stage.ssm.file.SsmFileValidationStage;
import com.codeshare.airline.schedule.orchestration.stage.ssm.parsing.SsmParsingStage;
import com.codeshare.airline.schedule.persistence.inbound.service.ScheduleInboundPersistenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SsmIngestionPipeline
        extends AbstractIngestionPipeline<SsmIngestionContext> {

    private final SsmFileValidationStage fileStage;
    private final SsmStructuralValidationStage structuralStage;
    private final SsmParsingStage parsingStage;
    private final SsmBusinessValidationStage businessStage;
    private final ScheduleInboundPersistenceService persistenceService;

    @Override
    protected String getFileId(SsmIngestionContext context) {
        return context.getInboundFile().getFileId();
    }

    @Override
    protected void fileValidation(SsmIngestionContext context) {
        fileStage.execute(context);
    }

    @Override
    protected void structuralValidation(SsmIngestionContext context) {
        structuralStage.execute(context);
    }

    @Override
    protected void parsing(SsmIngestionContext context) {
        parsingStage.execute(context);
    }

    @Override
    protected void businessValidation(SsmIngestionContext context) {
        businessStage.execute(context);
    }

    @Override
    protected boolean hasStructuralBlockingErrors(SsmIngestionContext context) {
        return context.getStructuralResult() != null &&
                context.getStructuralResult().hasBlockingErrors();
    }

    @Override
    protected boolean hasBusinessBlockingErrors(SsmIngestionContext context) {
        return context.getBusinessResult() != null &&
                context.getBusinessResult().hasBlockingErrors();
    }

    @Override
    protected void updateStatus(SsmIngestionContext context,
                                ProcessingStatus status) {

        persistenceService.updateStatus(
                context.getInboundFile(),
                status
        );
    }
}