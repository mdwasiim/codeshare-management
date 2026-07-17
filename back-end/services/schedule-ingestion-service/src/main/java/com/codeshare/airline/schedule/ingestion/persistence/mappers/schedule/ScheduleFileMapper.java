package com.codeshare.airline.schedule.ingestion.persistence.mappers.schedule;

import com.codeshare.airline.schedule.ingestion.domain.enums.ProcessingStatus;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleFileMetaDataDTO;
import com.codeshare.airline.schedule.ingestion.dto.schedule.ScheduleMessageDTO;
import com.codeshare.airline.schedule.ingestion.persistence.entities.schedule.ScheduleFileMetaDataEntity;
import com.codeshare.airline.schedule.ingestion.persistence.entities.schedule.ScheduleMessageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ScheduleFileMapper {

    private final ScheduleMessageMapper messageMapper;

    public ScheduleFileMetaDataEntity toEntity(ScheduleFileMetaDataDTO dto,
                                               List<ScheduleMessageDTO> envelopes) {

        if (dto == null) {
            throw new IllegalArgumentException("ScheduleFileMetaDataDTO cannot be null");
        }

        ScheduleFileMetaDataEntity entity = new ScheduleFileMetaDataEntity();

        /* ================= BASIC ================= */

        entity.setFileId(dto.getFileId());
        entity.setLoadId(dto.getLoadId());
        entity.setFileName(dto.getFileName());

        /* ================= TYPE ================= */

        entity.setMessageType(dto.getMessageType());
        entity.setSourceType(dto.getSourceType());
        entity.setScheduleProfile(dto.getScheduleProfile());
        entity.setAirlineCode(dto.getAirlineCode());
        entity.setPartnerCode(dto.getPartnerCode());

        /* ================= FILE INFO ================= */

        entity.setFileSizeBytes(dto.getFileSizeBytes());
        entity.setChecksum(dto.getChecksum());
        entity.setReceivedAt(
                dto.getReceivedAt() != null ? dto.getReceivedAt() : Instant.now()
        );

        /* ================= PROCESSING ================= */

        entity.setProcessingStatus(
                dto.getProcessingStatus() != null
                        ? dto.getProcessingStatus()
                        : ProcessingStatus.RECEIVED
        );

        entity.setProcessedAt(dto.getProcessedAt());
        entity.setFailedTimestamp(dto.getFailedTimestamp());
        entity.setErrorCode(dto.getErrorCode());

        /* ================= CHILDREN ================= */

        if (envelopes != null) {
            for (ScheduleMessageDTO envelopeDTO : envelopes) {

                ScheduleMessageEntity envelopeEntity =
                        messageMapper.toEntity(envelopeDTO);

                if (envelopeEntity != null) {

                    // 🔥 Ensure messageType always present
                    if (envelopeEntity.getMessageType() == null) {
                        envelopeEntity.setMessageType(entity.getMessageType());
                    }

                    entity.addEnvelope(envelopeEntity);
                }
            }
        }

        return entity;
    }

    public ScheduleFileMetaDataDTO toDTO(ScheduleFileMetaDataEntity entity) {
        throw new UnsupportedOperationException("Use concrete subclass if needed");
    }
}
