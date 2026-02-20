package com.codeshare.airline.auth.utils.mappers;

import com.codeshare.airline.auth.entities.RolePermission;
import com.codeshare.airline.core.dto.tenant.RolePermissionDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = CSMMapperConfig.class)
public interface RolePermissionMapper extends CSMGenericMapper<RolePermission, RolePermissionDTO> {

}

