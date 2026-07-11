package com.codeshare.airline.master.messaging.mappers;

import com.codeshare.airline.platform.core.dto.master.messaging.ActionIdentifierDTO;
import com.codeshare.airline.platform.core.mapper.CSMGenericMapper;
import com.codeshare.airline.platform.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.messaging.entities.ActionIdentifier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CSMMapperConfig.class)
public interface ActionIdentifierMapper
        extends CSMGenericMapper<ActionIdentifier, ActionIdentifierDTO> {

    @Mapping(source = "messageType", target = "applicableMessageType")
    ActionIdentifierDTO toDTO(ActionIdentifier entity);

    @Mapping(source = "applicableMessageType", target = "messageType")
    ActionIdentifier toEntity(ActionIdentifierDTO dto);

    @Mapping(source = "applicableMessageType", target = "messageType")
    void updateEntityFromDto(ActionIdentifierDTO dto, @MappingTarget ActionIdentifier entity);
}
