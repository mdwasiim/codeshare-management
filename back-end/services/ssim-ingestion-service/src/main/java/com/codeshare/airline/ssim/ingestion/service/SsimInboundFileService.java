package com.codeshare.airline.ssim.ingestion.service;

import com.codeshare.airline.ssim.ingestion.mapper.SsimInboundFileMapper;
import com.codeshare.airline.ssim.persistence.inbound.entity.SsimInboundCarrier;
import com.codeshare.airline.ssim.persistence.inbound.entity.SsimInboundFile;
import com.codeshare.airline.ssim.persistence.inbound.entity.SsimInboundHeader;
import com.codeshare.airline.ssim.persistence.inbound.entity.SsimInboundTrailer;
import com.codeshare.airline.ssim.persistence.inbound.repository.SsimInboundCarrierRepository;
import com.codeshare.airline.ssim.persistence.inbound.repository.SsimInboundFileRepository;
import com.codeshare.airline.ssim.persistence.inbound.repository.SsimInboundHeaderRepository;
import com.codeshare.airline.ssim.persistence.inbound.repository.SsimInboundTrailerRepository;
import com.codeshare.airline.ssim.source.SsimProcessingStatus;
import com.codeshare.airline.ssim.source.SsimSourceFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class SsimInboundFileService {

    private final SsimInboundFileRepository ssimInboundFileRepository;
    private final SsimInboundHeaderRepository ssimInboundHeaderRepository;
    private final SsimInboundCarrierRepository ssimInboundCarrierRepository;
    private final SsimInboundTrailerRepository ssimInboundTrailerRepository;

    public SsimInboundFile create(SsimSourceFile sourceFile) {

        SsimInboundFile inbound =
                SsimInboundFileMapper.fromRawFile(sourceFile);

        // file size
        inbound.setFileSizeBytes(1l);

        inbound.setProcessingStatus(SsimProcessingStatus.RECEIVED);

        return ssimInboundFileRepository.save(inbound);
    }

    public void updateStatus(String fileId, SsimProcessingStatus status) {
        ssimInboundFileRepository.findById(fileId).ifPresent(file -> {
            file.setProcessingStatus(status);
            ssimInboundFileRepository.save(file);
        });
    }

    public void markFailed(String fileId, Exception ex) {
        ssimInboundFileRepository.findById(fileId).ifPresent(file -> {
            file.setProcessingStatus(SsimProcessingStatus.FAILED);
            file.setFailedAt(Instant.now());
            file.setErrorMessage(ex.getMessage());
            ssimInboundFileRepository.save(file);
        });
    }

    public void saveHeader(SsimInboundHeader header) {
        ssimInboundHeaderRepository.save(header);
    }

    public void saveCarrier(SsimInboundCarrier carrier) {
        ssimInboundCarrierRepository.save(carrier);
    }

    public void saveTrailer(SsimInboundTrailer trailer) {
        ssimInboundTrailerRepository.save(trailer);
    }
}
