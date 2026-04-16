package com.codeshare.airline.ingestion.persistence.mappers.ssim;

import com.codeshare.airline.ingestion.persistence.dto.common.ssim.SsimHeaderDTO;
import com.codeshare.airline.ingestion.persistence.entities.ssim.SsimHeaderEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SsimHeaderMapper {

    /* =========================================================
       DTO → ENTITY
       ========================================================= */

    public SsimHeaderEntity toEntity(SsimHeaderDTO dto) {

        if (dto == null) return null;

        SsimHeaderEntity entity = new SsimHeaderEntity();

        entity.setId(dto.getId());

        /* ================= SSIM FIELDS ================= */

        entity.setRecordType(dto.getRecordType());
        entity.setTitleOfContents(trim(dto.getTitleOfContents(), 34));
        entity.setSpare36To40(trim(dto.getSpare36To40(), 5));
        entity.setNumberOfSeasons(dto.getNumberOfSeasons());
        entity.setSpare42To191(trim(dto.getSpare42To191(), 150));
        entity.setDatasetSerialNumber(trim(dto.getDatasetSerialNumber(), 3));
        entity.setRecordSerialNumber(trim(dto.getRecordSerialNumber(), 6));

        return entity;
    }

    /* =========================================================
       ENTITY → DTO
       ========================================================= */

    public SsimHeaderDTO toDTO(SsimHeaderEntity entity) {

        if (entity == null) return null;

        SsimHeaderDTO dto = new SsimHeaderDTO();

        dto.setId(entity.getId());

        dto.setFileId(
                entity.getFile() != null
                        ? entity.getFile().getFileId()
                        : null
        );

        dto.setRecordType(entity.getRecordType());
        dto.setTitleOfContents(entity.getTitleOfContents());
        dto.setSpare36To40(entity.getSpare36To40());
        dto.setNumberOfSeasons(entity.getNumberOfSeasons());
        dto.setSpare42To191(entity.getSpare42To191());
        dto.setDatasetSerialNumber(entity.getDatasetSerialNumber());
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