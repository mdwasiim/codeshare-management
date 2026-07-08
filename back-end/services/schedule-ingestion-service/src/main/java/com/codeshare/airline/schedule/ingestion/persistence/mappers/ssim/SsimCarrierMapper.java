package com.codeshare.airline.schedule.ingestion.persistence.mappers.ssim;

import com.codeshare.airline.schedule.ingestion.dto.ssim.record.SsimCarrierDTO;
import com.codeshare.airline.schedule.ingestion.persistence.entities.ssim.SsimCarrierEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SsimCarrierMapper {

    /* =========================================================
       DTO ??? ENTITY
       ========================================================= */

    public SsimCarrierEntity toEntity(SsimCarrierDTO dto) {

        if (dto == null) return null;

        SsimCarrierEntity entity = new SsimCarrierEntity();

        entity.setId(dto.getId());
        /* ================= SSIM FIELDS ================= */

        entity.setRecordType(SsimRecordTypeMapper.toCode(dto.getRecordType()));
        entity.setTimeMode(dto.getTimeMode());
        entity.setAirlineCode(trim(dto.getAirlineCode(), 3));

        entity.setSpare6To10(trim(dto.getSpare6To10(), 5));
        entity.setSeason(trim(dto.getSeason(), 3));
        entity.setSpare14(trim(dto.getSpare14(), 1));

        entity.setValidityStartRaw(trim(dto.getValidityStartRaw(), 7));
        entity.setValidityEndRaw(trim(dto.getValidityEndRaw(), 7));
        entity.setCreationDateRaw(trim(dto.getCreationDateRaw(), 7));

        entity.setTitleOfData(trim(dto.getTitleOfData(), 29));
        entity.setReleaseDateRaw(trim(dto.getReleaseDateRaw(), 7));

        entity.setScheduleStatus(trim(dto.getScheduleStatus(), 1));
        entity.setCreatorReference(trim(dto.getCreatorReference(), 35));
        entity.setDuplicateDesignatorMarker(trim(dto.getDuplicateDesignatorMarker(), 1));

        entity.setGeneralInformation(trim(dto.getGeneralInformation(), 61));
        entity.setInflightServiceInfo(trim(dto.getInflightServiceInfo(), 19));
        entity.setElectronicTicketingInfo(trim(dto.getElectronicTicketingInfo(), 2));

        entity.setCreationTimeRaw(trim(dto.getCreationTimeRaw(), 4));
        entity.setRecordSerialNumber(trim(dto.getRecordSerialNumber(), 6));

        return entity;
    }

    /* =========================================================
       ENTITY ??? DTO
       ========================================================= */

    public SsimCarrierDTO toDTO(SsimCarrierEntity entity) {

        if (entity == null) return null;

        SsimCarrierDTO dto = new SsimCarrierDTO();

        dto.setId(entity.getId());

        dto.setFileId(
                entity.getFile() != null
                        ? entity.getFile().getFileId()
                        : null
        );

        dto.setRecordType(SsimRecordTypeMapper.fromCode(entity.getRecordType()));
        dto.setTimeMode(entity.getTimeMode());
        dto.setAirlineCode(entity.getAirlineCode());

        dto.setSpare6To10(entity.getSpare6To10());
        dto.setSeason(entity.getSeason());
        dto.setSpare14(entity.getSpare14());

        dto.setValidityStartRaw(entity.getValidityStartRaw());
        dto.setValidityEndRaw(entity.getValidityEndRaw());
        dto.setCreationDateRaw(entity.getCreationDateRaw());

        dto.setTitleOfData(entity.getTitleOfData());
        dto.setReleaseDateRaw(entity.getReleaseDateRaw());

        dto.setScheduleStatus(entity.getScheduleStatus());
        dto.setCreatorReference(entity.getCreatorReference());
        dto.setDuplicateDesignatorMarker(entity.getDuplicateDesignatorMarker());

        dto.setGeneralInformation(entity.getGeneralInformation());
        dto.setInflightServiceInfo(entity.getInflightServiceInfo());
        dto.setElectronicTicketingInfo(entity.getElectronicTicketingInfo());

        dto.setCreationTimeRaw(entity.getCreationTimeRaw());
        dto.setRecordSerialNumber(entity.getRecordSerialNumber());

        return dto;
    }

    /* =========================================================
       SSIM FIXED WIDTH HELPER
       ========================================================= */

    private String trim(String value, int maxLength) {
        if (value == null) return null;
        return value.length() > maxLength
                ? value.substring(0, maxLength)
                : value;
    }
}

