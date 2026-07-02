package com.codeshare.airline.master.messaging.mappers;

import com.codeshare.airline.core.dto.ssim.ActionCodeDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.messaging.entities.ActionCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CSMMapperConfig.class)
public interface ActionCodeMapper extends CSMGenericMapper<ActionCode, ActionCodeDTO> {

    @Mapping(source = "actionIdentifier.id", target = "actionIdentifierId")
    ActionCodeDTO toDTO(ActionCode entity);

    @Mapping(target = "actionIdentifier", ignore = true)
    ActionCode toEntity(ActionCodeDTO dto);

    @Mapping(target = "actionIdentifier", ignore = true)
    void updateEntityFromDto(ActionCodeDTO dto, @MappingTarget ActionCode entity);
}
