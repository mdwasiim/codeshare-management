package com.codeshare.airline.ssim.inbound.orchestration;

import com.codeshare.airline.ssim.inbound.domain.contex.SsimIngestionContext;
import com.codeshare.airline.ssim.inbound.orchestration.pipeline.SsimIngestionPipeline;
import com.codeshare.airline.ssim.inbound.parsing.processor.dto.SsimInboundFileDTO;
import com.codeshare.airline.ssim.inbound.persistence.service.SsimInboundFileService;
import com.codeshare.airline.ssim.inbound.source.SsimSourceFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DefaultSsimProcessor implements SsimProcessor {

    private final SsimInboundFileService inboundFileService;
    private final SsimIngestionPipeline pipeline;

    @Override
    public void process(SsimSourceFile sourceFile) {

        SsimInboundFileDTO inboundFile = inboundFileService.create(sourceFile);

        SsimIngestionContext context =  SsimIngestionContext.builder()
                .sourceFile(sourceFile)
                .inboundFile(inboundFile)
                .build();

        try {
            pipeline.execute(context);
        } catch (Exception ex) {
            log.error("SSIM processing failed for fileId={}", inboundFile.getFileId(), ex);
            inboundFileService.markFailed(  inboundFile.getFileId(),ex);
        }
    }
}

