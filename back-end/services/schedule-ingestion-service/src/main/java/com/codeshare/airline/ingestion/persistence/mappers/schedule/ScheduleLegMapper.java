package com.codeshare.airline.ingestion.persistence.mappers.schedule;

import com.codeshare.airline.ingestion.persistence.dto.schedule.ScheduleLegDTO;
import com.codeshare.airline.ingestion.persistence.entities.schedule.ScheduleLegEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduleLegMapper {

    private final ScheduleDataElementMapper deiMapper;

    /* =========================================================
       DTO → ENTITY
       ========================================================= */

    public ScheduleLegEntity toEntity(ScheduleLegDTO dto) {
        if (dto == null) return null;

        ScheduleLegEntity entity = new ScheduleLegEntity();

        /* ================= SEQUENCE ================= */

        entity.setLegSequence(dto.getLegSequenceNumber());

        /* ================= ROUTING ================= */

        entity.setOrigin(dto.getBoardPoint());
        entity.setDestination(dto.getOffPoint());

        /* ================= TIMING ================= */

        entity.setDepartureTime(dto.getDepartureTime());
        entity.setArrivalTime(dto.getArrivalTime());
        entity.setDepartureDayOffset(dto.getDepartureDayOffset()); // ✅ added
        entity.setArrivalDayOffset(dto.getArrivalDayOffset());

        /* ================= EQUIPMENT OVERRIDE ================= */

        entity.setAircraftType(dto.getAircraftType());
        entity.setAircraftConfiguration(dto.getAircraftConfiguration());
        entity.setServiceType(dto.getServiceType()); // ✅ added

        /* ================= DEI ================= */

        if (dto.getDeis() != null) {
            int deiSeq = 1;

            for (var d : dto.getDeis()) {
                if (d != null && (d.isLegLevel() || d.isSegmentLevel())) {

                    var deiEntity = deiMapper.toEntity(d);

                    if (deiEntity != null) {
                        if (deiEntity.getSequenceOrder() == null) {
                            deiEntity.setSequenceOrder(deiSeq++);
                        }
                        entity.addDei(deiEntity);
                    }
                }
            }
        }

        return entity;
    }

    /* =========================================================
       ENTITY → DTO
       ========================================================= */

    public ScheduleLegDTO toDTO(ScheduleLegEntity entity) {
        if (entity == null) return null;

        return ScheduleLegDTO.builder()

                /* ================= SEQUENCE ================= */

                .legSequenceNumber(entity.getLegSequence())

                /* ================= ROUTING ================= */

                .boardPoint(entity.getOrigin())
                .offPoint(entity.getDestination())

                /* ================= TIMING ================= */

                .departureTime(entity.getDepartureTime())
                .arrivalTime(entity.getArrivalTime())
                .arrivalDayOffset(entity.getArrivalDayOffset())

                /* ================= EQUIPMENT ================= */

                .aircraftType(entity.getAircraftType())
                .aircraftConfiguration(entity.getAircraftConfiguration())

                /* ================= DEI ================= */

                .deis(
                        entity.getSafeDeis()
                                .stream()
                                .map(deiMapper::toDTO)
                                .toList()
                )

                .build();
    }
}