package com.codeshare.airline.auth.utils.mappers;

import com.codeshare.airline.auth.entities.identity.Permission;
import com.codeshare.airline.common.utils.mapper.audit.AuditMapper;
import com.codeshare.airline.common.auth.model.PermissionDTO;
import com.codeshare.airline.common.utils.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = AuditMapper.class)
public interface PermissionMapper extends GenericMapper<Permission, PermissionDTO> {

}

