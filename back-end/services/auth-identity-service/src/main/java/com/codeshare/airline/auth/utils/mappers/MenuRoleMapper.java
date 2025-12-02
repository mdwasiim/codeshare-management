package com.codeshare.airline.auth.utils.mappers;

import com.codeshare.airline.auth.entities.menu.MenuRole;
import com.codeshare.airline.common.auth.model.MenuRoleDTO;
import com.codeshare.airline.common.utils.mapper.GenericMapper;
import com.codeshare.airline.common.utils.mapper.audit.AuditMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { AuditMapper.class })
public interface MenuRoleMapper extends GenericMapper<MenuRole, MenuRoleDTO> {


}
