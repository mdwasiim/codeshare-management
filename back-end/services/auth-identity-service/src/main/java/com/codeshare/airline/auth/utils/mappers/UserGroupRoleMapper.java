package com.codeshare.airline.auth.utils.mappers;

import com.codeshare.airline.auth.entities.authorization.UserGroupRole;
import com.codeshare.airline.common.auth.model.UserGroupRoleDTO;
import com.codeshare.airline.common.utils.mapper.GenericMapper;
import com.codeshare.airline.common.utils.mapper.audit.AuditMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { AuditMapper.class })
public interface UserGroupRoleMapper extends GenericMapper<UserGroupRole, UserGroupRoleDTO> {

}
