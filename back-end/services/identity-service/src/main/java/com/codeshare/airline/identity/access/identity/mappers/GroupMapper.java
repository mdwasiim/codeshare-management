package com.codeshare.airline.identity.access.identity.mappers;

import com.codeshare.airline.identity.access.identity.entities.Group;
import com.codeshare.airline.platform.core.dto.tenant.GroupDTO;
import com.codeshare.airline.platform.core.mapper.CSMGenericMapper;
import com.codeshare.airline.platform.core.mapper.CSMMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface GroupMapper extends CSMGenericMapper<Group, GroupDTO> {

}

