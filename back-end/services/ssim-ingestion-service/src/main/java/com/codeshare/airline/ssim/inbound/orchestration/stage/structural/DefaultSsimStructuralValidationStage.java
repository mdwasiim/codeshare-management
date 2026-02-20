package com.codeshare.airline.ssim.inbound.orchestration.stage.structural;

import com.codeshare.airline.ssim.inbound.domain.contex.SsimIngestionContext;
import com.codeshare.airline.ssim.inbound.domain.enums.SsimProcessingStatus;
import com.codeshare.airline.ssim.inbound.orchestration.stage.file.profile.SsimProfileValidationStage;
import com.codeshare.airline.ssim.inbound.persistence.service.SsimInboundFileService;
import com.codeshare.airline.ssim.inbound.validation.model.ValidationResult;
import com.codeshare.airline.ssim.inbound.orchestration.stage.file.extension.DefaultSsimFileExtensionStage;
import com.codeshare.airline.ssim.inbound.orchestration.stage.file.extension.SsimFileExtensionStage;
import com.codeshare.airline.ssim.inbound.validation.structural.StructuralValidationEngine;
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

        String fileId = context.getInboundFile().getFileId();

        structuralValidation(fileId, context);
    }

    private void structuralValidation(String fileId, SsimIngestionContext context) {

        inboundFileService.updateStatus(fileId,SsimProcessingStatus.STRUCTURAL_VALIDATING);

        ValidationResult result = context.getSourceFile().withStream(engine::validate);

        reportService.saveAll(fileId, result.getMessages());

        context.setStructuralResult(result);

        if (result.hasBlockingErrors()) {
            inboundFileService.updateStatus(fileId, SsimProcessingStatus.STRUCTURAL_FAILED);
            throw new RuntimeException( "Structural validation failed for fileId=" + fileId);
        }
    }

}
