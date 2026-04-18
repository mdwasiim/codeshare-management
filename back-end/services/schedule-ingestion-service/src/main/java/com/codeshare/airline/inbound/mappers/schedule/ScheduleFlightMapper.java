package com.codeshare.airline.inbound.mappers.schedule;

import com.codeshare.airline.inbound.dto.schedule.ScheduleFlightDTO;
import com.codeshare.airline.inbound.entities.schedule.ScheduleFlightEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class ScheduleFlightMapper {

    private final ScheduleLegMapper legMapper;
    private final ScheduleDataElementMapper deiMapper;
    private final SchedulePeriodMapper periodMapper;

    /* =========================================================
       DTO → ENTITY
       ========================================================= */

    public ScheduleFlightEntity toEntity(ScheduleFlightDTO dto) {
        if (dto == null) return null;

        ScheduleFlightEntity entity = new ScheduleFlightEntity();

        /* ================= IDENTITY ================= */

        entity.setCarrier(dto.getAirlineDesignator());
        entity.setFlightNumber(dto.getFlightNumber());
        entity.setSuffix(dto.getOperationalSuffix());

        /* ================= EQUIPMENT ================= */

        entity.setAircraftType(dto.getAircraftType());
        entity.setServiceType(dto.getServiceType());
        entity.setAircraftConfiguration(dto.getAircraftConfiguration());
        entity.setBookingDesignator(dto.getBookingDesignator());

        /* ================= PERIODS ================= */

        if (dto.getPeriods() != null) {
            for (var p : dto.getPeriods()) {
                var periodEntity = periodMapper.toEntity(p);
                if (periodEntity != null) {
                    entity.addPeriod(periodEntity);
                }
            }
        }

        /* ================= LEGS ================= */

        if (dto.getLegs() != null) {
            int legSeq = 1;

            for (var l : dto.getLegs()) {
                var legEntity = legMapper.toEntity(l);

                if (legEntity != null) {
                    if (legEntity.getLegSequence() == null) {
                        legEntity.setLegSequence(legSeq++);
                    }
                    entity.addLeg(legEntity);
                }
            }
        }

        /* ================= DEI ================= */

        if (dto.getDeis() != null) {
            int deiSeq = 1;

            for (var d : dto.getDeis()) {
                if (d.isFlightLevel()) {
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

        /* ================= SUPPLEMENTARY ================= */

        entity.setSupplementaryInfo(
                dto.getSupplementaryInfo() != null
                        ? new ArrayList<>(dto.getSupplementaryInfo())
                        : null
        );

        return entity;
    }

    /* =========================================================
       ENTITY → DTO
       ========================================================= */

    public ScheduleFlightDTO toDTO(ScheduleFlightEntity entity) {
        if (entity == null) return null;

        return ScheduleFlightDTO.builder()

                /* ================= IDENTITY ================= */

                .airlineDesignator(entity.getCarrier())
                .flightNumber(entity.getFlightNumber())
                .operationalSuffix(entity.getSuffix())

                /* ================= EQUIPMENT ================= */

                .aircraftType(entity.getAircraftType())
                .serviceType(entity.getServiceType())
                .aircraftConfiguration(entity.getAircraftConfiguration())
                .bookingDesignator(entity.getBookingDesignator())

                /* ================= PERIODS ================= */

                .periods(
                        entity.getPeriods()
                                .stream()
                                .map(periodMapper::toDTO)
                                .toList()
                )

                /* ================= LEGS ================= */

                .legs(
                        entity.getSafeLegs()
                                .stream()
                                .map(legMapper::toDTO)
                                .toList()
                )

                /* ================= DEI ================= */

                .deis(
                        entity.getSafeDeis()
                                .stream()
                                .map(deiMapper::toDTO)
                                .toList()
                )

                /* ================= SUPPLEMENTARY ================= */

                .supplementaryInfo(entity.getSupplementaryInfo())

                .build();
    }
}