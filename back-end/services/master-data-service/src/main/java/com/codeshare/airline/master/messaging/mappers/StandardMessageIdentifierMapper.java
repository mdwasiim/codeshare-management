package com.codeshare.airline.master.messaging.mappers;

import com.codeshare.airline.core.dto.master.messaging.StandardMessageIdentifierDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.master.messaging.entities.StandardMessageIdentifier;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface StandardMessageIdentifierMapper
        extends CSMGenericMapper<StandardMessageIdentifier, StandardMessageIdentifierDTO> {
}
