package com.codeshare.airline.ssim.inbound.orchestration.stage.parsing;

import com.codeshare.airline.ssim.inbound.domain.contex.SsimIngestionContext;
import com.codeshare.airline.ssim.inbound.domain.enums.SsimProcessingStatus;
import com.codeshare.airline.ssim.inbound.parsing.processor.dto.SsimInboundFileDTO;
import com.codeshare.airline.ssim.inbound.persistence.loader.SsimIngestionLoader;
import com.codeshare.airline.ssim.inbound.persistence.service.SsimInboundFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultSsimParsingStage implements SsimParsingStage {

    private final SsimIngestionLoader loader;
    private final SsimInboundFileService inboundFileService;

    @Override
    public void execute(SsimIngestionContext context) {
        SsimInboundFileDTO inboundFile = context.getInboundFile();
        String fileId = inboundFile.getFileId();

        inboundFileService.updateStatus(fileId,SsimProcessingStatus.PARSING);
        try {
            context.getSourceFile().consumeStream(is -> loader.loadStream(is, SsimInboundFileDTO.toEntity(inboundFile)));

        } catch (Exception ex) {
            inboundFileService.updateStatus(fileId,SsimProcessingStatus.PARTIAL);
            throw ex;
        }
    }
}

