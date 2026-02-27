package com.codeshare.airline.schedule.orchestration.stage.ssim.file;

import com.codeshare.airline.schedule.domain.contex.SsimIngestionContext;
import com.codeshare.airline.schedule.domain.common.ProcessingStatus;
import com.codeshare.airline.schedule.domain.common.ScheduleProfile;
import com.codeshare.airline.schedule.orchestration.stage.FileValidationStage;
import com.codeshare.airline.schedule.orchestration.stage.ssim.file.extension.SsimFileExtension;
import com.codeshare.airline.schedule.orchestration.stage.ssim.file.profile.SsimProfileValidationStage;
import com.codeshare.airline.schedule.persistence.ssim.service.SsimInboundFileService;
import com.codeshare.airline.schedule.validation.core.exceptiom.InvalidFileExtensionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SsimStructuralValidation implements FileValidationStage<SsimIngestionContext> {

    private final SsimFileExtension ssimFileExtensionStage;
    private final SsimProfileValidationStage profileDetector;
    private final SsimInboundFileService inboundFileService;

    @Override
    public void execute(SsimIngestionContext context) {

        fileExtensionStage(context);

        profileDetectionStage(context);
    }

    private void fileExtensionStage(SsimIngestionContext context) {


        try {
            ssimFileExtensionStage.validate(context.getSourceFile());
        } catch (InvalidFileExtensionException ex) {
            inboundFileService.updateStatus(context.getInboundFile(), ProcessingStatus.REJECTED);
            throw ex;
        }
    }

    private void profileDetectionStage(SsimIngestionContext context) {

        inboundFileService.updateStatus(context.getInboundFile(),  ProcessingStatus.DETECTING_PROFILE);
        try {
            ScheduleProfile profile = context.getSourceFile().withStream(profileDetector::detect);

            context.setProfile(profile);

            inboundFileService.updateProfile(context.getInboundFile(), profile);

            inboundFileService.updateStatus(context.getInboundFile(), ProcessingStatus.PROFILE_DETECTED);

        } catch (Exception ex) {
            inboundFileService.updateStatus(context.getInboundFile(), ProcessingStatus.FAILED);
            throw ex;
        }
    }

}
