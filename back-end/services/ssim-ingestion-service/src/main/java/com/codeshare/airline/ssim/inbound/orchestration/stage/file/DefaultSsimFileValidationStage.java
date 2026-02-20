package com.codeshare.airline.ssim.inbound.orchestration.stage.file;

import com.codeshare.airline.ssim.inbound.domain.contex.SsimIngestionContext;
import com.codeshare.airline.ssim.inbound.domain.enums.SsimProcessingStatus;
import com.codeshare.airline.ssim.inbound.domain.enums.SsimProfile;
import com.codeshare.airline.ssim.inbound.orchestration.stage.file.extension.DefaultSsimFileExtensionStage;
import com.codeshare.airline.ssim.inbound.orchestration.stage.file.profile.SsimProfileValidationStage;
import com.codeshare.airline.ssim.inbound.persistence.service.SsimInboundFileService;
import com.codeshare.airline.ssim.inbound.validation.exception.InvalidFileExtensionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultSsimFileValidationStage implements SsimFileValidationStage {

    private final DefaultSsimFileExtensionStage service;
    private final SsimProfileValidationStage profileDetector;
    private final SsimInboundFileService inboundFileService;

    @Override
    public void execute(SsimIngestionContext context) {

        String fileId = context.getInboundFile().getFileId();

        fileExtensionStage(fileId, context);

        profileDetectionStage(fileId, context);
    }

    private void fileExtensionStage(String fileId, SsimIngestionContext context) {


        try {
            service.validate(context.getSourceFile());
        } catch (InvalidFileExtensionException ex) {
            inboundFileService.updateStatus(fileId, SsimProcessingStatus.REJECTED);
            throw ex;
        }
    }

    private void profileDetectionStage(String fileId, SsimIngestionContext context) {

        inboundFileService.updateStatus(fileId,  SsimProcessingStatus.DETECTING_PROFILE);

        try {

            SsimProfile profile = context.getSourceFile().withStream(profileDetector::detect);

            context.setProfile(profile);

            inboundFileService.updateProfile(fileId, profile);

            inboundFileService.updateStatus(fileId,SsimProcessingStatus.PROFILE_DETECTED);

        } catch (Exception ex) {
            inboundFileService.updateStatus(fileId, SsimProcessingStatus.FAILED);
            throw ex;
        }
    }

}
