package com.codeshare.airline.ssim.processor;

import com.codeshare.airline.ssim.ingestion.detector.SsimProfileDetector;
import com.codeshare.airline.ssim.ingestion.loader.SsimIngestionLoader;
import com.codeshare.airline.ssim.ingestion.service.SsimInboundFileService;
import com.codeshare.airline.ssim.persistence.inbound.entity.SsimInboundFile;
import com.codeshare.airline.ssim.source.SsimProcessingStatus;
import com.codeshare.airline.ssim.source.SsimProfile;
import com.codeshare.airline.ssim.source.SsimSourceFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@RequiredArgsConstructor
@Slf4j
public class DefaultSsimProcessor implements SsimProcessor {

    private final SsimInboundFileService inboundFileService;
    private final SsimIngestionLoader ingestionLoader;
    private final SsimProfileDetector profileDetector;

    @Async("ssimExecutor") // REMOVE when SEDA is added
    @Override
    public void process(SsimSourceFile sourceFile) {

        SsimInboundFile inboundFile =
                inboundFileService.create(sourceFile);

        try {
            inboundFileService.updateStatus(
                    inboundFile.getFileId(),
                    SsimProcessingStatus.LOADING
            );

            // 1️⃣ Detect profile (peek-only, fresh stream)
            SsimProfile profile =
                    sourceFile.<SsimProfile>withStream(profileDetector::detect);

            // Optional persistence
            // inboundFileService.updateProfile(
            //        inboundFile.getFileId(), profile);

            // 2️⃣ Load SSIM (fresh stream again)
            sourceFile.withStream((java.util.function.Consumer<InputStream>) is ->
                    ingestionLoader.load(is, inboundFile)
            );

            // 3️⃣ Mark completed
            inboundFileService.updateStatus(
                    inboundFile.getFileId(),
                    SsimProcessingStatus.COMPLETED
            );

        } catch (Exception ex) {
            log.error(
                    "SSIM processing failed for fileId={}",
                    inboundFile.getFileId(),
                    ex
            );

            inboundFileService.markFailed(
                    inboundFile.getFileId(),
                    ex
            );
        }
    }
}
