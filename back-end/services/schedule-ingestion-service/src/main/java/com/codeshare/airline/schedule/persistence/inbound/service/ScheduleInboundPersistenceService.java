package com.codeshare.airline.schedule.persistence.inbound.service;

import com.codeshare.airline.schedule.domain.common.ProcessingStatus;
import com.codeshare.airline.schedule.parsing.asm.dto.AsmInboundMessage;
import com.codeshare.airline.schedule.parsing.ssm.dto.SsmInboundMessage;
import com.codeshare.airline.schedule.persistence.inbound.entity.ScheduleInboundFile;
import com.codeshare.airline.schedule.persistence.inbound.mapper.ScheduleInboundMapper;
import com.codeshare.airline.schedule.persistence.inbound.repository.ScheduleInboundFileRepository;
import com.codeshare.airline.schedule.source.ScheduleSourceFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleInboundPersistenceService {

    private final ScheduleInboundFileRepository fileRepository;
    private final ScheduleInboundMapper mapper;

    // =====================================================
    // 1️⃣ Create metadata (Idempotent)
    // =====================================================

    @Transactional
    public ScheduleInboundFile createIfNotExists(ScheduleSourceFile sourceFile) {

        Optional<ScheduleInboundFile> existing =
                fileRepository.findByFileId(sourceFile.getFileId());

        if (existing.isPresent()) {
            return existing.get();
        }

        ScheduleInboundFile file = new ScheduleInboundFile();

        file.setFileId(sourceFile.getFileId());
        file.setAirlineCode(sourceFile.getAirlineCode());
        file.setMessageType(sourceFile.getScheduleMessageType());
        file.setSourceType(sourceFile.getSourceType());
        file.setSourceSystem(sourceFile.getSourceSystem());
        file.setReceivedAt(sourceFile.getReceivedAt());
        file.setFileSizeBytes(sourceFile.getFileSizeBytes());
        file.setChecksum(sourceFile.getChecksum());
        file.setProcessingStatus(ProcessingStatus.RECEIVED);

        return fileRepository.save(file);
    }

    // =====================================================
    // 2️⃣ Save ASM parsed blocks
    // =====================================================

    @Transactional
    public void saveAsm(
            ScheduleInboundFile file,
            List<AsmInboundMessage> messages) {

        int seq = 1;

        for (AsmInboundMessage msg : messages) {
            file.getBlocks().add(
                    mapper.mapAsmBlock(msg, file, seq++)
            );
        }

        fileRepository.save(file);
    }

    // =====================================================
    // 3️⃣ Save SSM parsed blocks
    // =====================================================

    @Transactional
    public void saveSsm(
            ScheduleInboundFile file,
            List<SsmInboundMessage> messages) {

        int seq = 1;

        for (SsmInboundMessage msg : messages) {
            file.getBlocks().add(
                    mapper.mapSsmBlock(msg, file, seq++)
            );
        }

        fileRepository.save(file);
    }

    // =====================================================
    // 4️⃣ Update status
    // =====================================================

    @Transactional
    public void updateStatus(
            ScheduleInboundFile file,
            ProcessingStatus newStatus) {

        validateTransition(file.getProcessingStatus(), newStatus);

        file.setProcessingStatus(newStatus);

        fileRepository.save(file);
    }

    // =====================================================
    // 5️⃣ Mark failure
    // =====================================================

    @Transactional
    public void markFailed(
            ScheduleInboundFile file,
            Exception ex) {

        file.setProcessingStatus(ProcessingStatus.FAILED);
        file.setFailedTimestamp(Instant.now());
        file.setErrorMessage(truncate(ex.getMessage()));

        fileRepository.save(file);
    }

    private void validateTransition(
            ProcessingStatus current,
            ProcessingStatus next) {

        if (current == ProcessingStatus.COMPLETED) {
            throw new IllegalStateException(
                    "Cannot transition from COMPLETED to " + next
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