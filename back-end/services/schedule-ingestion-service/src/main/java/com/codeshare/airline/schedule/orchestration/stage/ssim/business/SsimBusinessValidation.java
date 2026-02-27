package com.codeshare.airline.schedule.orchestration.stage.ssim.business;

import com.codeshare.airline.schedule.domain.contex.SsimIngestionContext;
import com.codeshare.airline.schedule.domain.common.ProcessingStatus;
import com.codeshare.airline.schedule.orchestration.stage.ssim.file.extension.SsimFileExtensionStage;
import com.codeshare.airline.schedule.persistence.ssim.service.SsimInboundFileService;
import com.codeshare.airline.schedule.validation.business.ssim.BusinessValidationEngine;
import com.codeshare.airline.schedule.validation.core.exceptiom.BusinessValidationException;
import com.codeshare.airline.schedule.validation.model.ValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SsimBusinessValidation implements SsimBusinessValidationStage {

    private final BusinessValidationEngine engine;
    private final SsimFileExtensionStage ssimFileExtensionStage;
    private final SsimInboundFileService inboundFileService;

    @Override
    public void execute(SsimIngestionContext context) {

        inboundFileService.updateStatus(  context.getInboundFile(), ProcessingStatus.BUSINESS_VALIDATING);

        ValidationResult result = engine.validate(context.getParsedResult());

        context.withBusinessResult(result);

        ssimFileExtensionStage.saveAll(
                context.getInboundFile(),
                result.getMessages()
        );

        if (result.hasBlockingErrors()) {

            inboundFileService.updateStatus(
                    context.getInboundFile(),
                    ProcessingStatus.BUSINESS_FAILED
            );

            throw new BusinessValidationException("Business validation failed");
        }
    }
}

