package com.codeshare.airline.schedule.orchestration.stage.asm.file;

import com.codeshare.airline.schedule.domain.common.ProcessingStatus;
import com.codeshare.airline.schedule.domain.common.ScheduleProfile;
import com.codeshare.airline.schedule.domain.contex.AsmIngestionContext;
import com.codeshare.airline.schedule.domain.contex.SsimIngestionContext;
import com.codeshare.airline.schedule.orchestration.stage.ssim.file.extension.DefaultSsimFileExtensionStage;
import com.codeshare.airline.schedule.orchestration.stage.ssim.file.profile.SsimProfileValidationStage;
import com.codeshare.airline.schedule.persistence.ssim.service.SsimInboundFileService;
import com.codeshare.airline.schedule.validation.ssim.exception.InvalidFileExtensionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultAsmFileValidationStage implements AsmFileValidationStage {

    private final DefaultSsimFileExtensionStage service;
    private final SsimProfileValidationStage profileDetector;
    private final SsimInboundFileService inboundFileService;

    @Override
    public void execute(AsmIngestionContext context) {

        String fileId = context.getInboundFileId();

        fileExtensionStage(fileId, context);

        profileDetectionStage(fileId, context);
    }

    private void fileExtensionStage(String fileId, AsmIngestionContext context) {


        try {
            service.validate(context.getSourceFile());
        } catch (InvalidFileExtensionException ex) {
            inboundFileService.updateStatus(fileId, ProcessingStatus.REJECTED);
            throw ex;
        }
    }

    private void profileDetectionStage(String fileId, AsmIngestionContext context) {

        inboundFileService.updateStatus(fileId,  ProcessingStatus.DETECTING_PROFILE);

        try {

            ScheduleProfile profile = context.getSourceFile().withStream(profileDetector::detect);

            context.setProfile(profile);

            inboundFileService.updateProfile(fileId, profile);

            inboundFileService.updateStatus(fileId, ProcessingStatus.PROFILE_DETECTED);

        } catch (Exception ex) {
            inboundFileService.updateStatus(fileId, ProcessingStatus.FAILED);
            throw ex;
        }
    }

}
