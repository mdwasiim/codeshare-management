package com.codeshare.airline.auth.utils.mappers;

import com.codeshare.airline.auth.entities.rbac.Permission;
import com.codeshare.airline.common.auth.identity.model.PermissionDTO;
import com.codeshare.airline.common.services.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",config = GenericMapper.class,  unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PermissionMapper extends GenericMapper<Permission, PermissionDTO> {

}

