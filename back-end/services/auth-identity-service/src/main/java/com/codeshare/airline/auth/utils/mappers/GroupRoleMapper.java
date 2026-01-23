package com.codeshare.airline.auth.utils.mappers;

import com.codeshare.airline.core.dto.tenant.GroupRoleDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.auth.model.entities.GroupRole;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",config = CSMGenericMapper.class,  unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GroupRoleMapper extends CSMGenericMapper<GroupRole, GroupRoleDTO> {

}
