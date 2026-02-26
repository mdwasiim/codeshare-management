package com.codeshare.airline.schedule.orchestration.pipeline.ssim;

import com.codeshare.airline.schedule.domain.common.ProcessingStatus;
import com.codeshare.airline.schedule.domain.contex.SsimIngestionContext;
import com.codeshare.airline.schedule.orchestration.pipeline.AbstractIngestionPipeline;
import com.codeshare.airline.schedule.orchestration.stage.ssim.business.SsimBusinessValidationStage;
import com.codeshare.airline.schedule.orchestration.stage.ssim.file.SsimFileValidationStage;
import com.codeshare.airline.schedule.orchestration.stage.ssim.parsing.SsimParsingStage;
import com.codeshare.airline.schedule.orchestration.stage.ssim.structural.SsimStructuralValidationStage;
import com.codeshare.airline.schedule.persistence.ssim.service.SsimInboundFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SsimIngestionPipeline
        extends AbstractIngestionPipeline<SsimIngestionContext, ProcessingStatus> {

    private final SsimFileValidationStage fileStage;
    private final SsimStructuralValidationStage structuralStage;
    private final SsimBusinessValidationStage businessStage;

    private final SsimInboundFileService inboundFileService;

    private final SsimParsingStage parsingStage;

    @Override
    protected String getFileId(SsimIngestionContext context) {
        return context.getInboundFileId();
    }

    @Override
    protected void fileValidation(SsimIngestionContext context) {
        fileStage.execute(context);
    }

    @Override
    protected void structuralValidation(SsimIngestionContext context) {
        structuralStage.execute(context);
    }

    @Override
    protected void parsing(SsimIngestionContext context) {
        parsingStage.execute(context);
    }

    @Override
    protected void businessValidation(SsimIngestionContext context) {
        businessStage.execute(context);
    }

    @Override
    protected boolean hasStructuralBlockingErrors(SsimIngestionContext context) {
        return context.getStructuralResult() != null &&
                context.getStructuralResult().hasBlockingErrors();
    }

    @Override
    protected boolean hasBusinessBlockingErrors(SsimIngestionContext context) {
        return context.getBusinessResult() != null &&
                context.getBusinessResult().hasBlockingErrors();
    }

    @Override
    protected void updateStatus(SsimIngestionContext context,
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