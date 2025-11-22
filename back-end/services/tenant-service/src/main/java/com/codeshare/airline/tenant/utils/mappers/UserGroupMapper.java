package com.codeshare.airline.tenant.utils.mappers;

import com.codeshare.airline.common.tenant.model.UserGroupDTO;
import com.codeshare.airline.common.utils.mapper.GenericMapper;
import com.codeshare.airline.tenant.entities.UserGroup;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserGroupMapper extends GenericMapper<UserGroup, UserGroupDTO> {
}
