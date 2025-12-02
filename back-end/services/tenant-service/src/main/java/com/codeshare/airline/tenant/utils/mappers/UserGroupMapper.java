package com.codeshare.airline.tenant.utils.mappers;

import com.codeshare.airline.common.tenant.model.UserGroupDTO;
import com.codeshare.airline.common.utils.mapper.GenericMapper;
import com.codeshare.airline.common.utils.mapper.audit.AuditMapper;
import com.codeshare.airline.tenant.entities.UserGroup;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { AuditMapper.class })
public interface UserGroupMapper extends GenericMapper<UserGroup, UserGroupDTO> {

}
