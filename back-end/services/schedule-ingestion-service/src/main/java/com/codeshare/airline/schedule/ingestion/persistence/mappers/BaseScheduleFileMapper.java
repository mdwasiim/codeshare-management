package com.codeshare.airline.schedule.ingestion.persistence.mappers;

import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.schedule.ingestion.persistence.entities.schedule.ScheduleFileMetaDataEntity;

import java.time.Instant;

public abstract class BaseScheduleFileMapper {

    protected void mapToEntity(ScheduleFileMetaDataDTO dto, ScheduleFileMetaDataEntity entity) {

        entity.setFileId(dto.getFileId());
        entity.setLoadId(dto.getLoadId());
        entity.setFileName(dto.getFileName());

        entity.setMessageType(dto.getMessageType());
        entity.setSourceType(dto.getSourceType());
        entity.setScheduleProfile(dto.getScheduleProfile());

        entity.setProcessingStatus(dto.getProcessingStatus());

        entity.setAirlineCode(dto.getAirlineCode());
        entity.setPartnerCode(dto.getPartnerCode());

        entity.setFileSizeBytes(dto.getFileSizeBytes());
        entity.setChecksum(dto.getChecksum());

        entity.setReceivedAt(dto.getReceivedAt());
        entity.setProcessedAt(dto.getProcessedAt());
        entity.setFailedTimestamp(dto.getFailedTimestamp());

        entity.setErrorCode(dto.getErrorCode());

        //entity.setSequenceReference(dto.getSequenceReference());
        //entity.setCreatorReference(dto.getCreatorReference());
        entity.setReceivedAt(dto.getReceivedAt() != null ? dto.getReceivedAt() : Instant.now());
        entity.setVersion(dto.getVersion());
    }

    protected void mapBaseFieldsToDTO(ScheduleFileMetaDataEntity entity, ScheduleFileMetaDataDTO.ScheduleFileMetaDataDTOBuilder<?, ?> builder) {

        builder
                .id(entity.getId())
                .fileId(entity.getFileId())
                .loadId(entity.getLoadId())
                .fileName(entity.getFileName())

                .messageType(entity.getMessageType())
                .sourceType(entity.getSourceType())
                .scheduleProfile(entity.getScheduleProfile())

                .processingStatus(entity.getProcessingStatus())
                //.timeMode(entity.getTimeMode())

                .airlineCode(entity.getAirlineCode())
                .partnerCode(entity.getPartnerCode())

                .fileSizeBytes(entity.getFileSizeBytes())
                .checksum(entity.getChecksum())

                .receivedAt(entity.getReceivedAt())
                .processedAt(entity.getProcessedAt())
                .failedTimestamp(entity.getFailedTimestamp())

                .errorCode(entity.getErrorCode())

                //.sequenceReference(entity.getSequenceReference())
                //.creatorReference(entity.getCreatorReference())

                .version(entity.getVersion());
    }
}
