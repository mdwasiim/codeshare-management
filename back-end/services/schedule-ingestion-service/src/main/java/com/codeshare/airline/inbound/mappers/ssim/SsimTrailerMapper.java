package com.codeshare.airline.inbound.mappers.ssim;

import com.codeshare.airline.inbound.dto.common.ssim.SsimTrailerDTO;
import com.codeshare.airline.inbound.entities.ssim.SsimTrailerEntity;
import org.springframework.stereotype.Component;

@Component
public class SsimTrailerMapper {

    private SsimTrailerMapper() {}

    public static SsimTrailerDTO toDto(SsimTrailerEntity entity) {

        if (entity == null) {
            return null;
        }

        SsimTrailerDTO dto = new SsimTrailerDTO();

        dto.setId(entity.getId());

        dto.setFileId(
                entity.getFile() != null
                        ? entity.getFile().getFileId()
                        : null
        );

        dto.setRecordType(SsimRecordTypeMapper.fromCode(entity.getRecordType()));
        dto.setSpareByte2(entity.getSpareByte2());

        dto.setAirlineDesignator(entity.getAirlineDesignator());
        dto.setReleaseDateRaw(entity.getReleaseDateRaw());

        dto.setSpare13To187(entity.getSpare13To187());

        dto.setSerialCheckReference(entity.getSerialCheckReference());
        dto.setContinuationEndCode(entity.getContinuationEndCode());

        dto.setRecordSerialNumber(entity.getRecordSerialNumber());

        return dto;
    }


    public  SsimTrailerEntity toEntity(SsimTrailerDTO dto) {

        if (dto == null) {
            return null;
        }

        SsimTrailerEntity entity = new SsimTrailerEntity();

        entity.setId(dto.getId());

        entity.setRecordType(SsimRecordTypeMapper.toCode(dto.getRecordType()));
        entity.setSpareByte2(dto.getSpareByte2());

        entity.setAirlineDesignator(dto.getAirlineDesignator());
        entity.setReleaseDateRaw(dto.getReleaseDateRaw());

        entity.setSpare13To187(dto.getSpare13To187());

        entity.setSerialCheckReference(dto.getSerialCheckReference());
        entity.setContinuationEndCode(dto.getContinuationEndCode());

        entity.setRecordSerialNumber(dto.getRecordSerialNumber());

        return entity;
    }

}
