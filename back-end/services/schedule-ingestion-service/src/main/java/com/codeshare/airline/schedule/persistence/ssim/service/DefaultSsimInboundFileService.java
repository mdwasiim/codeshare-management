package com.codeshare.airline.schedule.persistence.ssim.service;

import com.codeshare.airline.schedule.domain.common.ProcessingStatus;
import com.codeshare.airline.schedule.domain.common.ScheduleProfile;
import com.codeshare.airline.schedule.parsing.ssim.dto.SsimInboundFileDTO;
import com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundCarrier;
import com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundFile;
import com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundHeader;
import com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundTrailer;
import com.codeshare.airline.schedule.persistence.ssim.repository.SsimInboundCarrierRepository;
import com.codeshare.airline.schedule.persistence.ssim.repository.SsimInboundFileRepository;
import com.codeshare.airline.schedule.persistence.ssim.repository.SsimInboundHeaderRepository;
import com.codeshare.airline.schedule.persistence.ssim.repository.SsimInboundTrailerRepository;
import com.codeshare.airline.schedule.persistence.ssim.mapper.SsimInboundFileMapper;
import com.codeshare.airline.schedule.source.ScheduleSourceFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

import static com.codeshare.airline.schedule.domain.common.ProcessingStatus.COMPLETED;

@Service
@RequiredArgsConstructor
@Transactional
public class DefaultSsimInboundFileService implements SsimInboundFileService {

    private final SsimInboundFileRepository fileRepository;
    private final SsimInboundHeaderRepository headerRepository;
    private final SsimInboundCarrierRepository carrierRepository;
    private final SsimInboundTrailerRepository trailerRepository;
    private final SsimInboundFileRepository ssimInboundFileRepository;

    @Override
    public SsimInboundFileDTO create(ScheduleSourceFile sourceFile) {

        SsimInboundFile inbound =   SsimInboundFileMapper.fromRawFile(sourceFile);

        inbound.setFileSizeBytes(sourceFile.getFileSizeBytes());
        inbound.setProcessingStatus(ProcessingStatus.RECEIVED);
        inbound.setLoadId(UUID.randomUUID());
        SsimInboundFile ssimInboundFile = fileRepository.save(inbound);

        return SsimInboundFileDTO.toDto(ssimInboundFile);
    }

    @Override
    public void updateStatus(String fileId, ProcessingStatus status) {

        SsimInboundFile file = fileRepository.findById(UUID.fromString(fileId))
                .orElseThrow(() ->
                        new IllegalStateException("Inbound file not found: " + fileId)
                );

        validateTransition(file.getProcessingStatus(), status);

        file.setProcessingStatus(status);

        fileRepository.save(file);
    }

    private void validateTransition(
            ProcessingStatus current,
            ProcessingStatus next) {

        if (current == COMPLETED) {
            throw new IllegalStateException(
                    "Cannot change status from COMPLETED to " + next
            );
        }
    }

    @Override
    public void markFailed(String fileId, Exception ex) {

        fileRepository.findById(UUID.fromString(fileId)).ifPresentOrElse(file -> {

            if (file.getProcessingStatus() != ProcessingStatus.STRUCTURAL_FAILED &&
                    file.getProcessingStatus() != ProcessingStatus.BUSINESS_FAILED) {

                file.setProcessingStatus(ProcessingStatus.FAILED);
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


    @Override
    public void saveHeader(SsimInboundHeader header) {
        headerRepository.save(header);
    }

    @Override
    public void saveCarrier(SsimInboundCarrier carrier) {
        SsimInboundCarrier ssimInboundCarrier = carrierRepository.save(carrier);

        SsimInboundFile inboundFile = ssimInboundCarrier.getInboundFile();
        inboundFile.setAirlineCode(carrier.getAirlineCode().trim());
        fileRepository.save(inboundFile);
    }

    @Override
    public void saveTrailer(SsimInboundTrailer trailer) {
        trailerRepository.save(trailer);
    }

    @Override
    public void updateProfile(String fileId, ScheduleProfile profile) {
        fileRepository.updateProfile(UUID.fromString(fileId), profile);
    }

}
