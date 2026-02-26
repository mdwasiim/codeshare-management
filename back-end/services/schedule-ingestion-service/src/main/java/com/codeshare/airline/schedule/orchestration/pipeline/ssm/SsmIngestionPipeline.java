package com.codeshare.airline.schedule.orchestration.pipeline.ssm;


import com.codeshare.airline.schedule.domain.common.ProcessingStatus;
import com.codeshare.airline.schedule.domain.contex.SsmIngestionContext;
import com.codeshare.airline.schedule.orchestration.pipeline.AbstractIngestionPipeline;
import com.codeshare.airline.schedule.orchestration.stage.ssm.SsmStructuralValidationStage;
import com.codeshare.airline.schedule.orchestration.stage.ssm.business.SsmBusinessValidationStage;
import com.codeshare.airline.schedule.orchestration.stage.ssm.file.SsmFileValidationStage;
import com.codeshare.airline.schedule.orchestration.stage.ssm.parsing.SsmParsingStage;
import com.codeshare.airline.schedule.persistence.ssm.service.SsmInboundFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SsmIngestionPipeline
        extends AbstractIngestionPipeline<SsmIngestionContext, ProcessingStatus> {

    private final SsmFileValidationStage fileStage;
    private final SsmStructuralValidationStage structuralStage;
    private final SsmParsingStage parsingStage;
    private final SsmBusinessValidationStage businessStage;
    private final SsmInboundFileService inboundFileService;

    @Override
    protected String getFileId(SsmIngestionContext context) {
        return context.getInboundFileId();
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

        inboundFileService.updateStatus(
                context.getInboundFileId(),
                status
        );
    }

    @Override
    protected ProcessingStatus parsingStatus() {
        return ProcessingStatus.PARSING;
    }

    @Override
    protected ProcessingStatus businessValidatingStatus() {
        return ProcessingStatus.BUSINESS_VALIDATING;
    }

    @Override
    protected ProcessingStatus completedStatus() {
        return ProcessingStatus.COMPLETED;
    }
}