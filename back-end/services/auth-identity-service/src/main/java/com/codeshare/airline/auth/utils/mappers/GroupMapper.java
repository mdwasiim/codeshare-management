package com.codeshare.airline.auth.utils.mappers;

import com.codeshare.airline.auth.entities.Group;
import com.codeshare.airline.core.dto.tenant.GroupDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface GroupMapper extends CSMGenericMapper<Group, GroupDTO> {

}

