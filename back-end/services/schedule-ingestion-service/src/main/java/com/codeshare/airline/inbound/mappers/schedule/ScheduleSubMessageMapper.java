package com.codeshare.airline.inbound.mappers.schedule;

import com.codeshare.airline.inbound.domain.enums.ProcessingStatus;
import com.codeshare.airline.inbound.dto.schedule.ScheduleSubMessageDTO;
import com.codeshare.airline.inbound.entities.schedule.ScheduleFlightEntity;
import com.codeshare.airline.inbound.entities.schedule.ScheduleSubMessageEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ScheduleSubMessageMapper {

    private final ScheduleFlightMapper flightMapper;

    public ScheduleSubMessageEntity toEntity(ScheduleSubMessageDTO dto) {
        if (dto == null) return null;

        ScheduleSubMessageEntity entity = new ScheduleSubMessageEntity();

        /* ================= CORE ================= */

        entity.setActionType(dto.getActionType());

        // 🔥 DO NOT default here — handled by parent
        entity.setMessageSequenceNumber(dto.getMessageSequenceNumber());
        entity.setMessageReference(dto.getMessageReference());
        entity.setTimeMode(dto.getTimeMode());

        entity.setRawMessage(
                dto.getRawMessage() != null && !dto.getRawMessage().isBlank()
                        ? dto.getRawMessage()
                        : null
        );

        // 🔥 default processing status
        entity.setProcessingStatus(ProcessingStatus.RECEIVED);

        /* ================= FLIGHTS ================= */

        if (dto.getFlights() != null) {
            int flightSeq = 1;

            for (var flightDTO : dto.getFlights()) {
                ScheduleFlightEntity flightEntity = flightMapper.toEntity(flightDTO);

                if (flightEntity != null) {
                    // 🔥 ensure sequence always set
                    if (flightEntity.getFlightSequenceNumber() == null) {
                        flightEntity.setFlightSequenceNumber(flightSeq++);
                    }

                    entity.addFlight(flightEntity);
                }
            }
        }

        return entity;
    }

    public ScheduleSubMessageDTO toDTO(ScheduleSubMessageEntity entity) {
        if (entity == null) return null;

        return ScheduleSubMessageDTO.builder()

                .actionType(entity.getActionType())
                .messageSequenceNumber(entity.getMessageSequenceNumber())
                .messageReference(entity.getMessageReference())
                .timeMode(entity.getTimeMode())
                .rawMessage(entity.getRawMessage())
                .processingStatus(entity.getProcessingStatus() != null ? entity.getProcessingStatus().name() : null)
                .errorMessage(entity.getErrorMessage())

                .flights(
                        entity.getFlights() != null
                                ? entity.getFlights().stream()
                                .map(flightMapper::toDTO)
                                .toList()
                                : List.of()
                )

                .build();
    }
}
