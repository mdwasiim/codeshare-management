package com.codeshare.airline.identity.access.identity.mappers;

import com.codeshare.airline.identity.access.identity.entities.Role;
import com.codeshare.airline.platform.core.dto.tenant.RoleDTO;
import com.codeshare.airline.platform.core.mapper.CSMGenericMapper;
import com.codeshare.airline.platform.core.mapper.CSMMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface RoleMapper extends CSMGenericMapper<Role, RoleDTO> {
}
