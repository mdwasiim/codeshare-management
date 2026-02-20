package com.codeshare.airline.ssim.inbound.orchestration.stage.business;

import com.codeshare.airline.ssim.inbound.domain.contex.SsimIngestionContext;
import com.codeshare.airline.ssim.inbound.domain.enums.SsimProcessingStatus;
import com.codeshare.airline.ssim.inbound.orchestration.stage.file.extension.SsimFileExtensionStage;
import com.codeshare.airline.ssim.inbound.persistence.service.SsimInboundFileService;
import com.codeshare.airline.ssim.inbound.validation.business.BusinessValidationEngine;
import com.codeshare.airline.ssim.inbound.validation.exception.BusinessValidationException;
import com.codeshare.airline.ssim.inbound.validation.model.ValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultSsimBusinessValidationStage  implements SsimBusinessValidationStage {

    private final BusinessValidationEngine engine;
    private final SsimFileExtensionStage reportService;
    private final SsimInboundFileService inboundFileService;

    @Override
    public void execute(SsimIngestionContext context) {

        inboundFileService.updateStatus(  context.getInboundFile().getFileId(),SsimProcessingStatus.BUSINESS_VALIDATING);

        ValidationResult result = engine.validate(context.getInboundFile());

        context.withBusinessResult(result);

        reportService.saveAll(
                context.getInboundFile().getFileId(),
                result.getMessages()
        );

        if (result.hasBlockingErrors()) {

            inboundFileService.updateStatus(
                    context.getInboundFile().getFileId(),
                    SsimProcessingStatus.BUSINESS_FAILED
            );

            throw new BusinessValidationException("Business validation failed");
        }
    }
}

