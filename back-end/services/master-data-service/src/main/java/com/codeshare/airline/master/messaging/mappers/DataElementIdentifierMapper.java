package com.codeshare.airline.master.messaging.mappers;

import com.codeshare.airline.platform.core.dto.master.messaging.DataElementIdentifierDTO;
import com.codeshare.airline.platform.core.mapper.CSMGenericMapper;
import com.codeshare.airline.platform.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.messaging.entities.DataElementIdentifier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CSMMapperConfig.class)
public interface DataElementIdentifierMapper
        extends CSMGenericMapper<DataElementIdentifier, DataElementIdentifierDTO> {

    @Mapping(source = "standardMessageIdentifier.id", target = "standardMessageIdentifierId")
    DataElementIdentifierDTO toDTO(DataElementIdentifier entity);

    @Mapping(target = "standardMessageIdentifier", ignore = true)
    DataElementIdentifier toEntity(DataElementIdentifierDTO dto);

    @Mapping(target = "standardMessageIdentifier", ignore = true)
    void updateEntityFromDto(DataElementIdentifierDTO dto, @MappingTarget DataElementIdentifier entity);
}
