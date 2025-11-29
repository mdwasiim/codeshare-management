package com.codeshare.airline.auth.utils.mappers;

import com.codeshare.airline.auth.entities.identity.Role;
import com.codeshare.airline.common.audit.AuditMapper;
import com.codeshare.airline.common.auth.model.RoleDTO;
import com.codeshare.airline.common.utils.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = AuditMapper.class)
public interface RoleMapper extends GenericMapper<Role, RoleDTO> {
}
