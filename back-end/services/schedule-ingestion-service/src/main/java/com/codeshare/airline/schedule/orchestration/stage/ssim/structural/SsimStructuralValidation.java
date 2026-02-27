package com.codeshare.airline.schedule.orchestration.stage.ssim.structural;

import com.codeshare.airline.schedule.domain.common.ProcessingStatus;
import com.codeshare.airline.schedule.domain.contex.SsimIngestionContext;
import com.codeshare.airline.schedule.orchestration.stage.ssim.file.extension.SsimFileExtensionStage;
import com.codeshare.airline.schedule.persistence.ssim.service.SsimInboundFileService;
import com.codeshare.airline.schedule.validation.model.ValidationResult;
import com.codeshare.airline.schedule.validation.structural.ssim.StructuralValidationEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SsimStructuralValidation implements SsimStructuralValidationStage {

    private final SsimInboundFileService inboundFileService;
    private final StructuralValidationEngine engine;
    private final SsimFileExtensionStage reportService;

    @Override
    public void execute(SsimIngestionContext context) {

        structuralValidation(context);
    }

    private void structuralValidation(SsimIngestionContext context) {

        inboundFileService.updateStatus(context.getInboundFile(), ProcessingStatus.STRUCTURAL_VALIDATING);

        ValidationResult result = context.getSourceFile().withStream(engine::validate);

        reportService.saveAll(context.getInboundFile(), result.getMessages());

        context.setStructuralResult(result);

        if (result.hasBlockingErrors()) {
            inboundFileService.updateStatus(context.getInboundFile(), ProcessingStatus.STRUCTURAL_FAILED);
            throw new RuntimeException( "Structural validation failed for fileId=" + context.getSourceFile().getFileId());
        }
    }

}
