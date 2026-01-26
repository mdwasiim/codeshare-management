package com.codeshare.airline.auth.utils.mappers;

import com.codeshare.airline.auth.entities.Permission;
import com.codeshare.airline.core.dto.tenant.PermissionDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",config = CSMGenericMapper.class,  unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PermissionMapper extends CSMGenericMapper<Permission, PermissionDTO> {

}

