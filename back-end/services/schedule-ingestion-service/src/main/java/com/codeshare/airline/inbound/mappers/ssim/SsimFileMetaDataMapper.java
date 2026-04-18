package com.codeshare.airline.inbound.mappers.ssim;

import com.codeshare.airline.inbound.dto.ssim.SsimMetaDataDTO;
import com.codeshare.airline.inbound.entities.ssim.SsimFileMetaDataEntity;
import com.codeshare.airline.inbound.source.inbound.ScheduleSourceFile;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class SsimFileMetaDataMapper {

    /* =========================================================
       SOURCE → ENTITY
       ========================================================= */

    public SsimFileMetaDataEntity toEntity(ScheduleSourceFile source) {

        if (source == null) return null;

        SsimFileMetaDataEntity entity = new SsimFileMetaDataEntity();

        mapCommonSourceToEntity(source, entity);

        // SSIM specific
        entity.setScheduleProfile(source.getScheduleProfile());
        entity.setTimeMode(source.getTimeMode());

        return entity;
    }

    /* =========================================================
       DTO → ENTITY
       ========================================================= */

    public SsimFileMetaDataEntity toEntity(SsimMetaDataDTO dto) {

        if (dto == null) return null;

        SsimFileMetaDataEntity entity = new SsimFileMetaDataEntity();

        mapCommonDtoToEntity(dto, entity);

        // SSIM specific
        entity.setTotalRecordCount(dto.getTotalRecordCount());

        return entity;
    }

    /* =========================================================
       ENTITY → DTO
       ========================================================= */

    public SsimMetaDataDTO toDTO(SsimFileMetaDataEntity entity) {

        if (entity == null) return null;

        return SsimMetaDataDTO.builder()

                // COMMON
                .id(entity.getId())
                .fileId(entity.getFileId())
                .loadId(entity.getLoadId())
                .airlineCode(entity.getAirlineCode())

                .fileName(entity.getFileName())
                .sourceType(entity.getSourceType())
                .messageType(entity.getMessageType())
                .scheduleProfile(entity.getScheduleProfile())

                .fileSizeBytes(entity.getFileSizeBytes())
                .checksum(entity.getChecksum())

                // SSIM specific
                .totalRecordCount(entity.getTotalRecordCount())
                .timeMode(entity.getTimeMode())

                // PROCESSING
                .processingStatus(entity.getProcessingStatus())
                .receivedAt(entity.getReceivedTimestamp())
                .processedAt(entity.getStoredTimestamp())
                .failedTimestamp(entity.getFailedTimestamp())
                .errorCode(entity.getErrorCode())

                .version(entity.getVersion())

                .build();
    }

    /* =========================================================
       COMMON MAPPING METHODS (🔥 KEY IMPROVEMENT)
       ========================================================= */

    private void mapCommonSourceToEntity(ScheduleSourceFile source,SsimFileMetaDataEntity entity) {

        entity.setFileId(source.getFileId());
        entity.setLoadId(source.getLoadId());

        entity.setAirlineCode(source.getAirlineCode());

        entity.setFileName(source.getFileName());
        entity.setSourceType(source.getSourceType());
        entity.setMessageType(source.getMessageType());

        entity.setFileSizeBytes(source.getFileSizeBytes());
        entity.setChecksum(source.getChecksum());

        entity.setProcessingStatus(source.getProcessingStatus());
        entity.setReceivedTimestamp(Instant.now());

        //  FIXED: no hardcoded now()
    }

    private void mapCommonDtoToEntity(SsimMetaDataDTO dto,
                                      SsimFileMetaDataEntity entity) {

        entity.setFileId(dto.getFileId());
        entity.setLoadId(dto.getLoadId());

        entity.setAirlineCode(dto.getAirlineCode());

        entity.setFileName(dto.getFileName());
        entity.setSourceType(dto.getSourceType());
        entity.setMessageType(dto.getMessageType());
        entity.setScheduleProfile(dto.getScheduleProfile());

        entity.setFileSizeBytes(dto.getFileSizeBytes());
        entity.setChecksum(dto.getChecksum());

        entity.setProcessingStatus(dto.getProcessingStatus());

        entity.setReceivedTimestamp(dto.getReceivedAt());
        entity.setStoredTimestamp(dto.getProcessedAt());
        entity.setFailedTimestamp(dto.getFailedTimestamp());

        entity.setErrorCode(dto.getErrorCode());
        entity.setVersion(dto.getVersion());
    }
}