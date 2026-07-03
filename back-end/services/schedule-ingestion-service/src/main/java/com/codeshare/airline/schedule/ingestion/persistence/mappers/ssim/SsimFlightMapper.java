package com.codeshare.airline.schedule.ingestion.persistence.mappers.ssim;

import com.codeshare.airline.schedule.ingestion.dto.common.ssim.SsimFlightDTO;
import com.codeshare.airline.schedule.ingestion.persistence.entities.ssim.SsimCarrierEntity;
import com.codeshare.airline.schedule.ingestion.persistence.entities.ssim.SsimDataElementEntity;
import com.codeshare.airline.schedule.ingestion.persistence.entities.ssim.SsimFlightEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SsimFlightMapper {

    private final SsimDataElementMapper deiMapper;

    /* =========================================================
       DTO → ENTITY
       ========================================================= */

    public SsimFlightEntity toEntity(SsimFlightDTO dto, SsimCarrierEntity carrier) {

        if (dto == null) return null;

        SsimFlightEntity entity = new SsimFlightEntity();

        entity.setId(dto.getId());

        entity.setCarrier(carrier);
        /* ================= HEADER ================= */

        entity.setRecordType(SsimRecordTypeMapper.toCode(dto.getRecordType()));
        entity.setOperationalSuffix(trim(dto.getOperationalSuffix(), 1));
        entity.setAirlineCode(trim(dto.getAirlineCode(), 3));
        entity.setFlightNumber(trim(dto.getFlightNumber(), 4));
        entity.setItineraryVariationIdentifier(trim(dto.getItineraryVariationIdentifier(), 2));
        entity.setLegSequenceNumber(dto.getLegSequenceNumber());
        entity.setServiceType(trim(dto.getServiceType(), 1));

        /* ================= PERIOD ================= */

        entity.setOperatingPeriodStartRaw(trim(dto.getOperatingPeriodStartRaw(), 7));
        entity.setOperatingPeriodEndRaw(trim(dto.getOperatingPeriodEndRaw(), 7));
        entity.setOperatingDays(trim(dto.getOperatingDays(), 7));

        entity.setFrequencyRate(trim(dto.getFrequencyRate(), 1));

        /* ================= DEPARTURE ================= */

        entity.setDepartureStation(trim(dto.getDepartureStation(), 3));
        entity.setPassengerStd(trim(dto.getPassengerStd(), 4));
        entity.setAircraftStd(trim(dto.getAircraftStd(), 4));
        entity.setDepartureUtcVariation(trim(dto.getDepartureUtcVariation(), 5));
        entity.setDepartureTerminal(trim(dto.getDepartureTerminal(), 2));

        /* ================= ARRIVAL ================= */

        entity.setArrivalStation(trim(dto.getArrivalStation(), 3));
        entity.setAircraftSta(trim(dto.getAircraftSta(), 4));
        entity.setPassengerSta(trim(dto.getPassengerSta(), 4));
        entity.setArrivalUtcVariation(trim(dto.getArrivalUtcVariation(), 5));
        entity.setArrivalTerminal(trim(dto.getArrivalTerminal(), 2));

        /* ================= EQUIPMENT ================= */

        entity.setAircraftType(trim(dto.getAircraftType(), 3));
        entity.setPassengerReservationBookingDesignator(trim(dto.getPassengerReservationBookingDesignator(), 20));
        entity.setPassengerReservationBookingModifier(trim(dto.getPassengerReservationBookingModifier(), 5));
        entity.setMealServiceNote(trim(dto.getMealServiceNote(), 10));

        /* ================= JOINT ================= */

        entity.setJointOperationAirlineDesignators(trim(dto.getJointOperationAirlineDesignators(), 9));
        entity.setMinimumConnectingTimeStatus(trim(dto.getMinimumConnectingTimeStatus(), 2));
        entity.setSecureFlightIndicator(trim(dto.getSecureFlightIndicator(), 1));
        entity.setSpare123To127(trim(dto.getSpare123To127(), 5));
        entity.setItineraryVariationOverflow(trim(dto.getItineraryVariationOverflow(), 1));

        entity.setAircraftOwner(trim(dto.getAircraftOwner(), 3));
        entity.setCockpitCrewEmployer(trim(dto.getCockpitCrewEmployer(), 3));
        entity.setCabinCrewEmployer(trim(dto.getCabinCrewEmployer(), 3));

        entity.setOnwardAirlineDesignator(trim(dto.getOnwardAirlineDesignator(), 3));
        entity.setOnwardFlightNumber(trim(dto.getOnwardFlightNumber(), 4));
        entity.setAircraftRotationLayover(trim(dto.getAircraftRotationLayover(), 1));
        entity.setOnwardOperationalSuffix(trim(dto.getOnwardOperationalSuffix(), 1));

        /* ================= RESTRICTIONS ================= */

        entity.setSpare147(trim(dto.getSpare147(), 1));
        entity.setFlightTransitLayover(trim(dto.getFlightTransitLayover(), 1));
        entity.setOperatingAirlineDisclosure(trim(dto.getOperatingAirlineDisclosure(), 1));
        entity.setTrafficRestrictionCode(trim(dto.getTrafficRestrictionCode(), 11));
        entity.setTrafficRestrictionOverflow(trim(dto.getTrafficRestrictionOverflow(), 1));
        entity.setSpare162To172(trim(dto.getSpare162To172(), 11));

        /* ================= CONFIG ================= */

        entity.setAircraftConfigurationVersion(trim(dto.getAircraftConfigurationVersion(), 20));
        entity.setDateVariation(trim(dto.getDateVariation(), 2));

        /* ================= FOOTER ================= */

        entity.setRecordSerialNumber(trim(dto.getRecordSerialNumber(), 6));

        /* ================= DEIs ================= */

        if (dto.getDeis() != null) {
            dto.getDeis().forEach(d -> {
                SsimDataElementEntity dei = deiMapper.toEntity(d, entity);
                dei.setFlight(entity); // 🔥 RELATIONSHIP
                entity.getDeis().add(dei);
            });
        }

        return entity;
    }

    /* =========================================================
       ENTITY → DTO
       ========================================================= */

    public SsimFlightDTO toDTO(SsimFlightEntity entity) {

        if (entity == null) return null;

        SsimFlightDTO dto = new SsimFlightDTO();

        dto.setId(entity.getId());

        dto.setRecordType(SsimRecordTypeMapper.fromCode(entity.getRecordType()));
        dto.setOperationalSuffix(entity.getOperationalSuffix());
        dto.setAirlineCode(entity.getAirlineCode());
        dto.setFlightNumber(entity.getFlightNumber());
        dto.setItineraryVariationIdentifier(entity.getItineraryVariationIdentifier());
        dto.setLegSequenceNumber(entity.getLegSequenceNumber());
        dto.setServiceType(entity.getServiceType());

        dto.setOperatingPeriodStartRaw(entity.getOperatingPeriodStartRaw());
        dto.setOperatingPeriodEndRaw(entity.getOperatingPeriodEndRaw());
        dto.setOperatingDays(entity.getOperatingDays());
        dto.setFrequencyRate(entity.getFrequencyRate());

        dto.setDepartureStation(entity.getDepartureStation());
        dto.setPassengerStd(entity.getPassengerStd());
        dto.setAircraftStd(entity.getAircraftStd());
        dto.setDepartureUtcVariation(entity.getDepartureUtcVariation());
        dto.setDepartureTerminal(entity.getDepartureTerminal());

        dto.setArrivalStation(entity.getArrivalStation());
        dto.setAircraftSta(entity.getAircraftSta());
        dto.setPassengerSta(entity.getPassengerSta());
        dto.setArrivalUtcVariation(entity.getArrivalUtcVariation());
        dto.setArrivalTerminal(entity.getArrivalTerminal());

        dto.setAircraftType(entity.getAircraftType());
        dto.setPassengerReservationBookingDesignator(entity.getPassengerReservationBookingDesignator());
        dto.setPassengerReservationBookingModifier(entity.getPassengerReservationBookingModifier());
        dto.setMealServiceNote(entity.getMealServiceNote());

        dto.setJointOperationAirlineDesignators(entity.getJointOperationAirlineDesignators());
        dto.setMinimumConnectingTimeStatus(entity.getMinimumConnectingTimeStatus());
        dto.setSecureFlightIndicator(entity.getSecureFlightIndicator());
        dto.setSpare123To127(entity.getSpare123To127());
        dto.setItineraryVariationOverflow(entity.getItineraryVariationOverflow());

        dto.setAircraftOwner(entity.getAircraftOwner());
        dto.setCockpitCrewEmployer(entity.getCockpitCrewEmployer());
        dto.setCabinCrewEmployer(entity.getCabinCrewEmployer());
        dto.setOnwardAirlineDesignator(entity.getOnwardAirlineDesignator());
        dto.setOnwardFlightNumber(entity.getOnwardFlightNumber());
        dto.setAircraftRotationLayover(entity.getAircraftRotationLayover());
        dto.setOnwardOperationalSuffix(entity.getOnwardOperationalSuffix());

        dto.setSpare147(entity.getSpare147());
        dto.setFlightTransitLayover(entity.getFlightTransitLayover());
        dto.setOperatingAirlineDisclosure(entity.getOperatingAirlineDisclosure());
        dto.setTrafficRestrictionCode(entity.getTrafficRestrictionCode());
        dto.setTrafficRestrictionOverflow(entity.getTrafficRestrictionOverflow());
        dto.setSpare162To172(entity.getSpare162To172());

        dto.setAircraftConfigurationVersion(entity.getAircraftConfigurationVersion());
        dto.setDateVariation(entity.getDateVariation());

        dto.setRecordSerialNumber(entity.getRecordSerialNumber());

        if (entity.getDeis() != null) {
            entity.getDeis().forEach(dei -> dto.getDeis().add(deiMapper.toDTO(dei)));
        }

        return dto;
    }

    private String trim(String value, int maxLength) {
        if (value == null) return null;
        return value.length() > maxLength
                ? value.substring(0, maxLength)
                : value;
    }
}
