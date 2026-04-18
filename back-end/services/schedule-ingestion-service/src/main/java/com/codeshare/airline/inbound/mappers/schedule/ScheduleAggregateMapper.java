package com.codeshare.airline.inbound.mappers.schedule;

import com.codeshare.airline.inbound.domain.enums.ProcessingStatus;
import com.codeshare.airline.inbound.dto.schedule.ScheduleMessageDTO;
import com.codeshare.airline.inbound.entities.schedule.ScheduleFileMetaDataEntity;
import com.codeshare.airline.inbound.entities.schedule.ScheduleMessageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduleAggregateMapper {

    private final ScheduleMessageMapper scheduleMessageMapper;

    public ScheduleMessageEntity toEntity(ScheduleFileMetaDataEntity meta, ScheduleMessageDTO scheduleMessageDTO) {

        if (meta == null) {
            throw new IllegalArgumentException("File metadata cannot be null");
        }

        if (scheduleMessageDTO == null) {
            throw new IllegalArgumentException("ScheduleMessageDTO cannot be null");
        }

        ScheduleMessageEntity envelope = scheduleMessageMapper.toEntity(scheduleMessageDTO);

        if (envelope == null) {
            throw new IllegalStateException("Mapped ScheduleMessageEntity is null");
        }

        /* ================= LINKING ================= */

        // 🔥 IMPORTANT: maintain bidirectional consistency
        meta.addEnvelope(envelope);

        /* ================= SAFETY ================= */

        if (envelope.getMessageType() == null) {
            envelope.setMessageType(meta.getMessageType());
        }

        if (envelope.getProcessingStatus() == null) {
            envelope.setProcessingStatus(ProcessingStatus.RECEIVED);
        }

        return envelope;
    }
}