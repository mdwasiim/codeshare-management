package com.codeshare.airline.auth.utils.mappers;

import com.codeshare.airline.auth.entities.GroupRole;
import com.codeshare.airline.core.dto.tenant.GroupRoleDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import org.mapstruct.Mapper;


@Mapper(config = CSMMapperConfig.class)
public interface GroupRoleMapper extends CSMGenericMapper<GroupRole, GroupRoleDTO> {

}
