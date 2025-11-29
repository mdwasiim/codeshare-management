package com.codeshare.airline.auth.utils.mappers;

import com.codeshare.airline.auth.entities.authorization.GroupRole;
import com.codeshare.airline.common.audit.AuditMapper;
import com.codeshare.airline.common.auth.model.GroupRoleDTO;
import com.codeshare.airline.common.utils.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = AuditMapper.class)
public interface GroupRoleMapper extends GenericMapper<GroupRole, GroupRoleDTO> {


}
