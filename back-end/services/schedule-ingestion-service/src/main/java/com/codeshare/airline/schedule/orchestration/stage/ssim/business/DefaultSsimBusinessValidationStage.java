package com.codeshare.airline.schedule.orchestration.stage.ssim.business;

import com.codeshare.airline.schedule.domain.contex.SsimIngestionContext;
import com.codeshare.airline.schedule.domain.common.ProcessingStatus;
import com.codeshare.airline.schedule.orchestration.stage.ssim.file.extension.SsimFileExtensionStage;
import com.codeshare.airline.schedule.persistence.ssim.service.SsimInboundFileService;
import com.codeshare.airline.schedule.validation.ssim.business.BusinessValidationEngine;
import com.codeshare.airline.schedule.validation.ssim.exception.BusinessValidationException;
import com.codeshare.airline.schedule.validation.ssim.model.ValidationResult;
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

        inboundFileService.updateStatus(  context.getInboundFile().getFileId(), ProcessingStatus.BUSINESS_VALIDATING);

        ValidationResult result = engine.validate(context.getInboundFile());

        context.withBusinessResult(result);

        reportService.saveAll(
                context.getInboundFile().getFileId(),
                result.getMessages()
        );

        if (result.hasBlockingErrors()) {

            inboundFileService.updateStatus(
                    context.getInboundFile().getFileId(),
                    ProcessingStatus.BUSINESS_FAILED
            );

            throw new BusinessValidationException("Business validation failed");
        }
    }
}

