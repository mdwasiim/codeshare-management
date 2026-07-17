package com.codeshare.airline.schedule.ingestion.persistence.services.common;

import com.codeshare.airline.platform.core.enums.schedule.MessageType;
import com.codeshare.airline.schedule.ingestion.domain.enums.ProcessingStatus;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.schedule.ingestion.dto.ssim.SsimMetaDataDTO;
import com.codeshare.airline.schedule.ingestion.persistence.entities.schedule.ScheduleFileMetaDataEntity;
import com.codeshare.airline.schedule.ingestion.persistence.entities.ssim.SsimFileMetaDataEntity;
import com.codeshare.airline.schedule.ingestion.persistence.mappers.schedule.ScheduleFileMetaDataMapper;
import com.codeshare.airline.schedule.ingestion.persistence.mappers.ssim.SsimFileMetaDataMapper;
import com.codeshare.airline.schedule.ingestion.persistence.repositories.schedule.ScheduleFileMetaDataRepository;
import com.codeshare.airline.schedule.ingestion.persistence.repositories.ssim.SsimFileMetaDataRepository;
import com.codeshare.airline.schedule.ingestion.source.model.ScheduleSourceFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DefaultScheduleFileService implements ScheduleFileService {

    private final SsimFileMetaDataRepository ssimFileRepository;
    private final ScheduleFileMetaDataRepository scheduleFileRepository;

    private final SsimFileMetaDataMapper ssimMapper;
    private final ScheduleFileMetaDataMapper scheduleMapper;

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ScheduleFileMetaDataDTO> T createInbound(ScheduleSourceFile file, MessageType type) {

        return switch (type) {
            case ASM -> (T) createAsmInboundFileIfNotExists(file);
            case SSM -> (T) createSsmInboundFileIfNotExists(file);
            case SSIM -> (T) createSsimInboundFileIfNotExists(file);
        };
    }

    /* =========================================================
       ASM
       ========================================================= */

    public ScheduleFileMetaDataDTO createAsmInboundFileIfNotExists(ScheduleSourceFile sourceFile) {
        ScheduleFileMetaDataDTO dto = convertToAsmDTO(sourceFile);
        ScheduleFileMetaDataEntity entity =
                scheduleFileRepository.findByFileId(sourceFile.getFileId())
                        .orElseGet(() -> scheduleFileRepository.save(
                                scheduleMapper.toEntity(dto) //  FIXED
                        ));

        return scheduleMapper.toDto(entity);
    }

    /* =========================================================
       SSM
       ========================================================= */

    public ScheduleFileMetaDataDTO createSsmInboundFileIfNotExists(ScheduleSourceFile sourceFile) {
        ScheduleFileMetaDataDTO dto = convertToSsmDTO(sourceFile); //  FIXED

        ScheduleFileMetaDataEntity entity =
                scheduleFileRepository.findByFileId(dto.getFileId())
                        .orElseGet(() -> scheduleFileRepository.save(
                                scheduleMapper.toEntity(dto)
                        ));

        return scheduleMapper.toDto(entity);
    }

    /* =========================================================
       SSIM
       ========================================================= */

    public SsimMetaDataDTO createSsimInboundFileIfNotExists(ScheduleSourceFile sourceFile) {
        SsimFileMetaDataEntity entity =
                ssimFileRepository.findByFileId(sourceFile.getFileId())
                        .or(() -> findExistingSsimByChecksum(sourceFile))
                        .orElseGet(() -> ssimFileRepository.save(
                                ssimMapper.toEntity(sourceFile) //  FIXED
                        ));

        return ssimMapper.toDTO(entity); //  FIXED
    }

    private Optional<SsimFileMetaDataEntity> findExistingSsimByChecksum(ScheduleSourceFile sourceFile) {
        if (isBlank(sourceFile.getAirlineCode()) || isBlank(sourceFile.getChecksum())) {
            return Optional.empty();
        }

        Optional<SsimFileMetaDataEntity> existing = ssimFileRepository.findByAirlineCodeAndChecksum(
                sourceFile.getAirlineCode(),
                sourceFile.getChecksum()
        );

        existing.ifPresent(entity -> log.info(
                "SSIM file already exists for airline={} checksum={} existingFileId={} incomingFileId={} status={}",
                sourceFile.getAirlineCode(),
                sourceFile.getChecksum(),
                entity.getFileId(),
                sourceFile.getFileId(),
                entity.getProcessingStatus()
        ));

        return existing;
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    /* =========================================================
       CONVERTERS
       ========================================================= */

    private ScheduleFileMetaDataDTO convertToSsmDTO(ScheduleSourceFile source) {

        return ScheduleFileMetaDataDTO.builder()
                .fileId(source.getFileId())
                .loadId(source.getLoadId())
                .fileName(source.getFileName())

                .messageType(source.getMessageType())
                .sourceType(source.getSourceType())
                .airlineCode(source.getAirlineCode())

                .scheduleProfile(source.getScheduleProfile()) // 
                .timeMode(source.getTimeMode())               // 

                .fileSizeBytes(source.getFileSizeBytes())
                .checksum(source.getChecksum())

                .processingStatus(source.getProcessingStatus())
                .build();
    }

    private ScheduleFileMetaDataDTO convertToAsmDTO(ScheduleSourceFile source) {
        return ScheduleFileMetaDataDTO.builder()
                .fileId(source.getFileId())
                .loadId(source.getLoadId())
                .fileName(source.getFileName())

                .messageType(source.getMessageType())
                .sourceType(source.getSourceType())
                .airlineCode(source.getAirlineCode())

                .scheduleProfile(source.getScheduleProfile())
                .timeMode(source.getTimeMode())

                .fileSizeBytes(source.getFileSizeBytes())
                .checksum(source.getChecksum())

                .processingStatus(source.getProcessingStatus())

                .build();
    }

    /* =========================================================
       STATUS UPDATE
       ========================================================= */

    @Override
    public void updateScheduleStatus(ScheduleFileMetaDataDTO metadata,
                                     ProcessingStatus status) {

        ProcessingStatus current = metadata.getProcessingStatus();

        if (!shouldUpdate(current, status)) {
            return;
        }

        updateEntity(metadata.getId(), status, metadata);
    }

    private void updateEntity(Long id, ProcessingStatus status, ScheduleFileMetaDataDTO metadata) {

        if (metadata instanceof SsimMetaDataDTO) {
            ssimFileRepository.findById(id)
                    .ifPresent(entity -> {
                        entity.setProcessingStatus(status);
                        applyTimestamps(entity, status);
                        ssimFileRepository.save(entity);
                    });

        } else {

            scheduleFileRepository.findById(id)
                    .ifPresent(entity -> {
                        entity.setProcessingStatus(status);
                        applyTimestamps(entity, status);
                        scheduleFileRepository.save(entity);
                    });
        }

        applyTimestamps(metadata, status);
        metadata.setProcessingStatus(status);
        /*if (metadata instanceof SsimMetaDataDTO ssim) {
            metadata = ssim.toBuilder()
                    .processingStatus(status)
                    .build();
        } else if (metadata instanceof SsmMetaDataDTO ssm) {
            metadata = ssm.toBuilder()
                    .processingStatus(status)
                    .build();
        } else if (metadata instanceof AsmMetaDataDTO asm) {
            metadata = asm.toBuilder()
                    .processingStatus(status)
                    .build();
        }*/
    }

    private boolean shouldUpdate(ProcessingStatus current, ProcessingStatus newStatus) {

        if (current == newStatus) return false;
        if (current == null) return true;

        return switch (current) {
            case RECEIVED -> newStatus == ProcessingStatus.VALIDATING
                    || newStatus == ProcessingStatus.FAILED;
            case VALIDATING -> newStatus == ProcessingStatus.PROCESSING
                    || newStatus == ProcessingStatus.FAILED;
            case PROCESSING -> newStatus == ProcessingStatus.COMPLETED
                    || newStatus == ProcessingStatus.PARTIAL
                    || newStatus == ProcessingStatus.FAILED;
            case PARTIAL, FAILED -> newStatus == ProcessingStatus.VALIDATING;
            case REJECTED, COMPLETED, SUCCESS -> false;
        };
    }

    private void applyTimestamps(SsimFileMetaDataEntity entity, ProcessingStatus status) {
        Instant now = Instant.now();
        if (status == ProcessingStatus.FAILED) {
            entity.setFailedTimestamp(now);
            return;
        }
        if (status == ProcessingStatus.COMPLETED || status == ProcessingStatus.SUCCESS || status == ProcessingStatus.PARTIAL) {
            entity.setStoredTimestamp(now);
        }
    }

    private void applyTimestamps(ScheduleFileMetaDataEntity entity, ProcessingStatus status) {
        Instant now = Instant.now();
        if (status == ProcessingStatus.FAILED) {
            entity.setFailedTimestamp(now);
            return;
        }
        if (status == ProcessingStatus.COMPLETED || status == ProcessingStatus.SUCCESS || status == ProcessingStatus.PARTIAL) {
            entity.setProcessedAt(now);
        }
    }

    private void applyTimestamps(ScheduleFileMetaDataDTO metadata, ProcessingStatus status) {
        Instant now = Instant.now();
        if (status == ProcessingStatus.FAILED) {
            metadata.setFailedTimestamp(now);
            return;
        }
        if (status == ProcessingStatus.COMPLETED || status == ProcessingStatus.SUCCESS || status == ProcessingStatus.PARTIAL) {
            metadata.setProcessedAt(now);
        }
    }
}
