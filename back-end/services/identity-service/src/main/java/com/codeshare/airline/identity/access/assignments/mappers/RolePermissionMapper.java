package com.codeshare.airline.identity.access.assignments.mappers;


import com.codeshare.airline.core.dto.tenant.RolePermissionDTO;
import com.codeshare.airline.identity.access.assignments.entities.RolePermission;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CSMMapperConfig.class)
public interface RolePermissionMapper extends CSMGenericMapper<RolePermission, RolePermissionDTO> {
    @Override
    @Mapping(target = "permissionId", source = "permission.id")
    @Mapping(target = "roleId", source = "role.id")
    RolePermissionDTO toDTO(RolePermission entity);
}

