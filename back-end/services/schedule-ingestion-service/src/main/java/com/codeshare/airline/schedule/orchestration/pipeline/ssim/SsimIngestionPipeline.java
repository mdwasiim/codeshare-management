package com.codeshare.airline.schedule.orchestration.pipeline.ssim;

import com.codeshare.airline.schedule.domain.common.ProcessingStatus;
import com.codeshare.airline.schedule.domain.contex.SsimIngestionContext;
import com.codeshare.airline.schedule.orchestration.pipeline.AbstractIngestionPipeline;
import com.codeshare.airline.schedule.orchestration.stage.ssim.business.SsimBusinessValidationStage;
import com.codeshare.airline.schedule.orchestration.stage.ssim.file.SsimStructuralValidationStage;
import com.codeshare.airline.schedule.orchestration.stage.ssim.parsing.SsimParsingStage;
import com.codeshare.airline.schedule.persistence.ssim.service.SsimInboundFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SsimIngestionPipeline
        extends AbstractIngestionPipeline<SsimIngestionContext> {

    private final SsimStructuralValidationStage fileStage;
    private final com.codeshare.airline.schedule.orchestration.stage.ssim.structural.SsimStructuralValidationStage structuralStage;
    private final SsimParsingStage parsingStage;
    private final SsimBusinessValidationStage businessStage;
    private final SsimInboundFileService inboundFileService;

    @Override
    protected String getFileId(SsimIngestionContext context) {
        return context.getInboundFile().getId().toString();
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
    protected void updateStatus(SsimIngestionContext context, ProcessingStatus status) {

        inboundFileService.updateStatus(context.getInboundFile(),status);
    }
}