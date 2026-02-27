package com.codeshare.airline.schedule.persistence.ssim.service;

import com.codeshare.airline.schedule.domain.common.ProcessingStatus;
import com.codeshare.airline.schedule.domain.common.ScheduleProfile;
import com.codeshare.airline.schedule.persistence.ssim.entity.SsimInboundFile;
import com.codeshare.airline.schedule.persistence.ssim.mapper.SsimInboundFileMapper;
import com.codeshare.airline.schedule.persistence.ssim.repository.SsimInboundFileRepository;
import com.codeshare.airline.schedule.source.ScheduleSourceFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DefaultSsimInboundFileService implements SsimInboundFileService {

    private final SsimInboundFileRepository fileRepository;

    @Override
    public SsimInboundFile create(ScheduleSourceFile sourceFile) {

        SsimInboundFile inbound =
                SsimInboundFileMapper.fromRawFile(sourceFile);

        inbound.setFileSizeBytes(sourceFile.getFileSizeBytes());
        inbound.setProcessingStatus(ProcessingStatus.RECEIVED);
        inbound.setLoadId(UUID.randomUUID());

        return fileRepository.save(inbound);
    }

    @Override
    public void updateStatus(SsimInboundFile file,
                             ProcessingStatus status) {

        validateTransition(file.getProcessingStatus(), status);

        file.setProcessingStatus(status);
        fileRepository.save(file);
    }

    @Override
    public void updateProfile(SsimInboundFile file,
                              ScheduleProfile profile) {

        file.setScheduleProfile(profile);
        fileRepository.save(file);
    }

    @Override
    public void markFailed(SsimInboundFile file,
                           Exception ex) {

        if (file.getProcessingStatus() != ProcessingStatus.STRUCTURAL_FAILED &&
                file.getProcessingStatus() != ProcessingStatus.BUSINESS_FAILED) {

            file.setProcessingStatus(ProcessingStatus.FAILED);
        }

        file.setFailedTimestamp(Instant.now());
        file.setErrorMessage(truncate(ex.getMessage()));

        fileRepository.save(file);
    }

    private void validateTransition(ProcessingStatus current,
                                    ProcessingStatus next) {

        if (current == ProcessingStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Cannot change status from COMPLETED to " + next
            );
        }
    }

    private String truncate(String message) {
        if (message == null) return null;
        return message.length() > 1000
                ? message.substring(0, 1000)
                : message;
    }
}