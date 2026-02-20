package com.codeshare.airline.ssim.inbound.orchestration.pipeline;

import com.codeshare.airline.ssim.inbound.domain.contex.SsimIngestionContext;
import com.codeshare.airline.ssim.inbound.domain.enums.SsimProcessingStatus;
import com.codeshare.airline.ssim.inbound.orchestration.stage.business.SsimBusinessValidationStage;
import com.codeshare.airline.ssim.inbound.orchestration.stage.file.SsimFileValidationStage;
import com.codeshare.airline.ssim.inbound.orchestration.stage.parsing.SsimParsingStage;
import com.codeshare.airline.ssim.inbound.orchestration.stage.structural.SsimStructuralValidationStage;
import com.codeshare.airline.ssim.inbound.persistence.service.SsimInboundFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SsimIngestionPipeline {

    private final SsimFileValidationStage ssimFileValidationStage;
    private final SsimStructuralValidationStage structuralStage;
    private final SsimParsingStage parsingStage;
    private final SsimBusinessValidationStage businessStage;

    private final SsimInboundFileService inboundFileService;

    public void execute(SsimIngestionContext context) {

        String fileId = context.getInboundFile().getFileId();

        // 1️⃣ FILE VALIDATION
        log.info("FILE stage for fileId={}", fileId);
        ssimFileValidationStage.execute(context);

        // 2️⃣ STRUCTURAL VALIDATION
        log.info("STRUCTURAL stage for fileId={}", fileId);
        structuralStage.execute(context);

        if (context.getStructuralResult() != null && context.getStructuralResult().hasBlockingErrors()) {
            return;
        }

        // 3️⃣ PARSING
        inboundFileService.updateStatus(fileId, SsimProcessingStatus.PARSING);
        log.info("PARSING stage for fileId={}", fileId);
        parsingStage.execute(context);


        //Update SSIM context Temporary
        /*SsimInboundFileDTO inboundFile = inboundFileService.getUpdatedSsimInboundFile(context.getSourceFile().getFileId());

        SsimIngestionContext context =  SsimIngestionContext.builder()
                .sourceFile(sourceFile)
                .inboundFile(inboundFile)
                .build();*/
        //End Later this block need to be removed



        // 4️⃣ BUSINESS VALIDATION
        inboundFileService.updateStatus(fileId, SsimProcessingStatus.BUSINESS_VALIDATING);
        log.info("BUSINESS stage for fileId={}", fileId);
        businessStage.execute(context);

        if (context.getBusinessResult() != null && context.getBusinessResult().hasBlockingErrors()) {
            return;
        }
        // 5️⃣ SUCCESS
        inboundFileService.updateStatus(fileId, SsimProcessingStatus.COMPLETED);
    }


}
