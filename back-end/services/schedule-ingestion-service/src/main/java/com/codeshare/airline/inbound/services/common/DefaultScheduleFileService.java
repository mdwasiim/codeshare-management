package com.codeshare.airline.inbound.services.common;

import com.codeshare.airline.enums.MessageType;
import com.codeshare.airline.inbound.domain.enums.ProcessingStatus;
import com.codeshare.airline.inbound.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.inbound.dto.ssim.SsimMetaDataDTO;
import com.codeshare.airline.inbound.entities.schedule.ScheduleFileMetaDataEntity;
import com.codeshare.airline.inbound.entities.ssim.SsimFileMetaDataEntity;
import com.codeshare.airline.inbound.mappers.schedule.ScheduleFileMetaDataMapper;
import com.codeshare.airline.inbound.mappers.ssim.SsimFileMetaDataMapper;
import com.codeshare.airline.inbound.repositories.schedule.ScheduleFileMetaDataRepository;
import com.codeshare.airline.inbound.repositories.ssim.SsimFileMetaDataRepository;
import com.codeshare.airline.inbound.source.inbound.ScheduleSourceFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
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
                        .orElseGet(() -> ssimFileRepository.save(
                                ssimMapper.toEntity(sourceFile) //  FIXED
                        ));

        return ssimMapper.toDTO(entity); //  FIXED
    }

    /* =========================================================
       CONVERTERS (🔥 MISSING PIECE YOU NEEDED)
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
       STATUS UPDATE (GOOD - KEEP AS IS)
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

    private void updateEntity(UUID id, ProcessingStatus status, ScheduleFileMetaDataDTO metadata) {

        if (metadata instanceof SsimMetaDataDTO) {
            ssimFileRepository.findById(id)
                    .ifPresent(entity -> {
                        entity.setProcessingStatus(status);
                        ssimFileRepository.save(entity);
                    });

        } else {

            scheduleFileRepository.findById(id)
                    .ifPresent(entity -> {
                        entity.setProcessingStatus(status);
                        scheduleFileRepository.save(entity);
                    });
        }

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

        return current == null || current.ordinal() <= newStatus.ordinal();
    }
}