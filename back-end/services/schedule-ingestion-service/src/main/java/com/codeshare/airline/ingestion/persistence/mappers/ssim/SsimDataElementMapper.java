package com.codeshare.airline.ingestion.persistence.mappers.ssim;

import com.codeshare.airline.ingestion.persistence.dto.common.ssim.SsimDataElementDTO;
import com.codeshare.airline.ingestion.persistence.entities.ssim.SsimDataElementEntity;
import com.codeshare.airline.ingestion.persistence.entities.ssim.SsimFlightEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SsimDataElementMapper {

    /* =========================================================
       DTO → ENTITY
       ========================================================= */

    public SsimDataElementEntity toEntity(SsimDataElementDTO dto,
                                                 SsimFlightEntity flight) {

        if (dto == null) return null;

        SsimDataElementEntity entity = new SsimDataElementEntity();

        entity.setId(dto.getId());

        /* ================= RELATIONSHIP ================= */

        entity.setFlight(flight);     // 🔥 REQUIRED

        /* ================= HEADER ================= */

        entity.setRecordType(dto.getRecordType());
        entity.setOperationalSuffix(trim(dto.getOperationalSuffix(), 1));
        entity.setAirlineCode(trim(dto.getAirlineCode(), 3));
        entity.setFlightNumber(trim(dto.getFlightNumber(), 4));
        entity.setItineraryVariationIdentifier(trim(dto.getItineraryVariationIdentifier(), 2));
        entity.setLegSequenceNumber(trim(dto.getLegSequenceNumber(), 2));
        entity.setServiceType(trim(dto.getServiceType(), 1));

        /* ================= STRUCTURE ================= */

        entity.setSpare15To27(trim(dto.getSpare15To27(), 13));
        entity.setItineraryVariationOverflow(trim(dto.getItineraryVariationOverflow(), 1));

        /* ================= SEGMENT IDENTIFIERS ================= */

        entity.setBoardPointIndicator(trim(dto.getBoardPointIndicator(), 1));
        entity.setOffPointIndicator(trim(dto.getOffPointIndicator(), 1));
        entity.setDataElementIdentifier(trim(dto.getDataElementIdentifier(), 3));
        entity.setBoardPoint(trim(dto.getBoardPoint(), 3));
        entity.setOffPoint(trim(dto.getOffPoint(), 3));

        /* ================= DATA ================= */

        entity.setDeiData(trim(dto.getDeiData(), 155));

        /* ================= FOOTER ================= */

        entity.setRecordSerialNumber(trim(dto.getRecordSerialNumber(), 6));

        return entity;
    }

    /* =========================================================
       ENTITY → DTO
       ========================================================= */

    public SsimDataElementDTO toDTO(SsimDataElementEntity entity) {

        if (entity == null) return null;

        SsimDataElementDTO dto = new SsimDataElementDTO();

        dto.setId(entity.getId());

        dto.setFlightLegId(
                entity.getFlight() != null
                        ? entity.getFlight().getId()
                        : null
        );

        dto.setRecordType(entity.getRecordType());
        dto.setOperationalSuffix(entity.getOperationalSuffix());
        dto.setAirlineCode(entity.getAirlineCode());
        dto.setFlightNumber(entity.getFlightNumber());
        dto.setItineraryVariationIdentifier(entity.getItineraryVariationIdentifier());
        dto.setLegSequenceNumber(entity.getLegSequenceNumber());
        dto.setServiceType(entity.getServiceType());

        dto.setSpare15To27(entity.getSpare15To27());
        dto.setItineraryVariationOverflow(entity.getItineraryVariationOverflow());

        dto.setBoardPointIndicator(entity.getBoardPointIndicator());
        dto.setOffPointIndicator(entity.getOffPointIndicator());
        dto.setDataElementIdentifier(entity.getDataElementIdentifier());
        dto.setBoardPoint(entity.getBoardPoint());
        dto.setOffPoint(entity.getOffPoint());

        dto.setDeiData(entity.getDeiData());

        dto.setRecordSerialNumber(entity.getRecordSerialNumber());
        return dto;
    }

    /* =========================================================
       FIXED WIDTH HELPER
       ========================================================= */

    private String trim(String value, int maxLength) {
        if (value == null) return null;
        return value.length() > maxLength
                ? value.substring(0, maxLength)
                : value;
    }
}