package com.codeshare.airline.schedule.orchestration.stage.ssim.parsing;

import com.codeshare.airline.schedule.domain.contex.SsimIngestionContext;
import com.codeshare.airline.schedule.domain.common.ProcessingStatus;
import com.codeshare.airline.schedule.parsing.ssim.dto.SsimInboundFileDTO;
import com.codeshare.airline.schedule.persistence.ssim.loader.SsimIngestionLoader;
import com.codeshare.airline.schedule.persistence.ssim.service.SsimInboundFileService;
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

        inboundFileService.updateStatus(fileId, ProcessingStatus.PARSING);
        try {
            context.getSourceFile().consumeStream(is -> loader.loadStream(is, SsimInboundFileDTO.toEntity(inboundFile)));

        } catch (Exception ex) {
            inboundFileService.updateStatus(fileId, ProcessingStatus.PARTIAL);
            throw ex;
        }
    }
}

