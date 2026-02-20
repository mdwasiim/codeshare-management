package com.codeshare.airline.auth.utils.mappers;

import com.codeshare.airline.auth.entities.Role;
import com.codeshare.airline.core.dto.tenant.RoleDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface RoleMapper extends CSMGenericMapper<Role, RoleDTO> {
}
