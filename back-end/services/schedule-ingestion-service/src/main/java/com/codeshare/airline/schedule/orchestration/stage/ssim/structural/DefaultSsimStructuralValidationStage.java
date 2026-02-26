package com.codeshare.airline.schedule.orchestration.stage.ssim.structural;

import com.codeshare.airline.schedule.domain.contex.SsimIngestionContext;
import com.codeshare.airline.schedule.domain.common.ProcessingStatus;
import com.codeshare.airline.schedule.orchestration.stage.ssim.file.profile.SsimProfileValidationStage;
import com.codeshare.airline.schedule.persistence.ssim.service.SsimInboundFileService;
import com.codeshare.airline.schedule.validation.ssim.model.ValidationResult;
import com.codeshare.airline.schedule.orchestration.stage.ssim.file.extension.DefaultSsimFileExtensionStage;
import com.codeshare.airline.schedule.orchestration.stage.ssim.file.extension.SsimFileExtensionStage;
import com.codeshare.airline.schedule.validation.ssim.structural.StructuralValidationEngine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultSsimStructuralValidationStage implements SsimStructuralValidationStage {

    private final DefaultSsimFileExtensionStage service;
    private final SsimProfileValidationStage profileDetector;
    private final SsimInboundFileService inboundFileService;
    private final StructuralValidationEngine engine;
    private final SsimFileExtensionStage reportService;

    @Override
    public void execute(SsimIngestionContext context) {

        String fileId = context.getInboundFileId();

        structuralValidation(fileId, context);
    }

    private void structuralValidation(String fileId, SsimIngestionContext context) {

        inboundFileService.updateStatus(fileId, ProcessingStatus.STRUCTURAL_VALIDATING);

        ValidationResult result = context.getSourceFile().withStream(engine::validate);

        reportService.saveAll(fileId, result.getMessages());

        context.withStructuralResult(result);

        if (result.hasBlockingErrors()) {
            inboundFileService.updateStatus(fileId, ProcessingStatus.STRUCTURAL_FAILED);
            throw new RuntimeException( "Structural validation failed for fileId=" + fileId);
        }
    }

}
