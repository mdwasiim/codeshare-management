package com.codeshare.airline.ssim.inbound.persistence.service;

import com.codeshare.airline.ssim.inbound.domain.enums.SsimProcessingStatus;
import com.codeshare.airline.ssim.inbound.domain.enums.SsimProfile;
import com.codeshare.airline.ssim.inbound.parsing.processor.dto.SsimInboundFileDTO;
import com.codeshare.airline.ssim.inbound.persistence.inbound.entity.SsimInboundCarrier;
import com.codeshare.airline.ssim.inbound.persistence.inbound.entity.SsimInboundFile;
import com.codeshare.airline.ssim.inbound.persistence.inbound.entity.SsimInboundHeader;
import com.codeshare.airline.ssim.inbound.persistence.inbound.entity.SsimInboundTrailer;
import com.codeshare.airline.ssim.inbound.persistence.inbound.repository.SsimInboundCarrierRepository;
import com.codeshare.airline.ssim.inbound.persistence.inbound.repository.SsimInboundFileRepository;
import com.codeshare.airline.ssim.inbound.persistence.inbound.repository.SsimInboundHeaderRepository;
import com.codeshare.airline.ssim.inbound.persistence.inbound.repository.SsimInboundTrailerRepository;
import com.codeshare.airline.ssim.inbound.persistence.mapper.SsimInboundFileMapper;
import com.codeshare.airline.ssim.inbound.source.SsimSourceFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

import static com.codeshare.airline.ssim.inbound.domain.enums.SsimProcessingStatus.COMPLETED;

@Service
@RequiredArgsConstructor
@Transactional
public class SsimInboundFileService {

    private final SsimInboundFileRepository fileRepository;
    private final SsimInboundHeaderRepository headerRepository;
    private final SsimInboundCarrierRepository carrierRepository;
    private final SsimInboundTrailerRepository trailerRepository;
    private final SsimInboundFileRepository ssimInboundFileRepository;

    public SsimInboundFileDTO create(SsimSourceFile sourceFile) {

        SsimInboundFile inbound =   SsimInboundFileMapper.fromRawFile(sourceFile);

        inbound.setFileSizeBytes(sourceFile.getFileSizeBytes());
        inbound.setProcessingStatus(SsimProcessingStatus.RECEIVED);
        inbound.setLoadId(UUID.randomUUID());
        SsimInboundFile ssimInboundFile = fileRepository.save(inbound);

        return SsimInboundFileDTO.toDto(ssimInboundFile);
    }

    public void updateStatus(String fileId, SsimProcessingStatus status) {

        SsimInboundFile file = fileRepository.findById(UUID.fromString(fileId))
                .orElseThrow(() ->
                        new IllegalStateException("Inbound file not found: " + fileId)
                );

        validateTransition(file.getProcessingStatus(), status);

        file.setProcessingStatus(status);

        fileRepository.save(file);
    }

    private void validateTransition(
            SsimProcessingStatus current,
            SsimProcessingStatus next) {

        if (current == COMPLETED) {
            throw new IllegalStateException(
                    "Cannot change status from COMPLETED to " + next
            );
        }
    }


    public void markFailed(String fileId, Exception ex) {

        fileRepository.findById(UUID.fromString(fileId)).ifPresentOrElse(file -> {

            if (file.getProcessingStatus() != SsimProcessingStatus.STRUCTURAL_FAILED &&
                    file.getProcessingStatus() != SsimProcessingStatus.BUSINESS_FAILED) {

                file.setProcessingStatus(SsimProcessingStatus.FAILED);
            }

            file.setFailedTimestamp(Instant.now());
            file.setErrorMessage(truncate(ex.getMessage()));

        }, () -> {
            throw new IllegalStateException("Inbound file not found: " + fileId);
        });
    }
    private String truncate(String message) {
        if (message == null) return null;
        return message.length() > 1000
                ? message.substring(0, 1000)
                : message;
    }


    public void saveHeader(SsimInboundHeader header) {
        headerRepository.save(header);
    }

    public void saveCarrier(SsimInboundCarrier carrier) {
        SsimInboundCarrier ssimInboundCarrier = carrierRepository.save(carrier);

        SsimInboundFile inboundFile = ssimInboundCarrier.getInboundFile();
        inboundFile.setAirlineCode(carrier.getAirlineCode().trim());
        fileRepository.save(inboundFile);
    }

    public void saveTrailer(SsimInboundTrailer trailer) {
        trailerRepository.save(trailer);
    }

    public void updateProfile(String fileId, SsimProfile profile) {
        fileRepository.updateProfile(UUID.fromString(fileId), profile);
    }

    public SsimInboundFileDTO getUpdatedSsimInboundFile(String fileId) {
        return null;


    }
}
