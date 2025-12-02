package com.codeshare.airline.auth.utils.mappers;

import com.codeshare.airline.auth.entities.identity.Group;
import com.codeshare.airline.common.utils.mapper.audit.AuditMapper;
import com.codeshare.airline.common.auth.model.GroupDTO;
import com.codeshare.airline.common.utils.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = AuditMapper.class)
public interface GroupMapper extends GenericMapper<Group, GroupDTO> {

}

