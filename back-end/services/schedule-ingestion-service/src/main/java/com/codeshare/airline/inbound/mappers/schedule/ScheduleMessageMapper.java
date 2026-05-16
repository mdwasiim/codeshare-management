package com.codeshare.airline.inbound.mappers.schedule;

import com.codeshare.airline.inbound.domain.enums.ProcessingStatus;
import com.codeshare.airline.inbound.dto.schedule.ScheduleSubMessageDTO;
import com.codeshare.airline.inbound.dto.schedule.ScheduleMessageDTO;
import com.codeshare.airline.inbound.entities.schedule.ScheduleSubMessageEntity;
import com.codeshare.airline.inbound.entities.schedule.ScheduleMessageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduleMessageMapper {

    private final ScheduleSubMessageMapper subMapper;

    /* =========================================================
       DTO → ENTITY
       ========================================================= */

    public ScheduleMessageEntity toEntity(ScheduleMessageDTO dto) {
        if (dto == null) return null;

        ScheduleMessageEntity entity = new ScheduleMessageEntity();

        /* ================= HEADER ================= */

        // 🔥 CRITICAL: ensure messageType is always set
        if (dto.getMessageType() == null) {
            throw new IllegalArgumentException("MessageType is mandatory for ScheduleMessage");
        }
        entity.setMessageType(dto.getMessageType());

        entity.setSource(dto.getSource());
        entity.setSender(dto.getSender());
        entity.setRecipient(dto.getRecipient());

        entity.setCreationDate(dto.getCreationDate()); // ✅ added
        entity.setCreationDateRaw(dto.getCreationDateRaw());
        entity.setCreationTime(dto.getCreationTime());

        entity.setMessageReference(dto.getMessageReference());
        entity.setCreatorReference(dto.getCreatorReference());

        entity.setTimeMode(dto.getTimeMode()); // ✅ added
        entity.setRawHeader(dto.getRawHeader());

        // 🔥 default processing status (important for ingestion)
        if (entity.getProcessingStatus() == null) {
            entity.setProcessingStatus(ProcessingStatus.RECEIVED); // or enum if you updated it
        }

        /* ================= SUB-MESSAGES ================= */

        if (dto.getMessages() != null) {
            int sequence = 1; // 🔥 sequence handling (important)

            for (ScheduleSubMessageDTO sub : dto.getMessages()) {
                ScheduleSubMessageEntity subEntity = subMapper.toEntity(sub);

                if (subEntity != null) {
                    // 🔥 ensure sequence is always set
                    if (subEntity.getMessageSequenceNumber() == null) {
                        subEntity.setMessageSequenceNumber(sequence++);
                    }

                    entity.addSubMessage(subEntity);
                }
            }
        }

        return entity;
    }

    /* =========================================================
       ENTITY → DTO
       ========================================================= */

    public ScheduleMessageDTO toDTO(ScheduleMessageEntity entity) {
        if (entity == null) return null;

        return ScheduleMessageDTO.builder()

                /* ================= HEADER ================= */

                .messageType(entity.getMessageType())
                .source(entity.getSource())
                .sender(entity.getSender())
                .recipient(entity.getRecipient())

                .creationDate(entity.getCreationDate())
                .creationDateRaw(entity.getCreationDateRaw())
                .creationTime(entity.getCreationTime())
                .timeMode(entity.getTimeMode())

                .messageReference(entity.getMessageReference())
                .creatorReference(entity.getCreatorReference())

                .rawHeader(entity.getRawHeader())
                .processingStatus(entity.getProcessingStatus() != null ? entity.getProcessingStatus().name() : null)
                .errorMessage(entity.getErrorMessage())

                /* ================= SUB-MESSAGES ================= */

                .messages(
                        entity.getSubMessages()
                                .stream()
                                .map(subMapper::toDTO)
                                .toList()
                )

                .build();
    }
}
