package com.codeshare.airline.identity.access.authorization.mappers;

import com.codeshare.airline.core.dto.tenant.PermissionDTO;
import com.codeshare.airline.identity.access.authorization.entities.Permission;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface PermissionMapper extends CSMGenericMapper<Permission, PermissionDTO> {

}

