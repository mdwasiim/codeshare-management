package com.codeshare.airline.auth.utils.mappers;

import com.codeshare.airline.auth.entities.menu.Menu;
import com.codeshare.airline.common.utils.mapper.audit.AuditMapper;
import com.codeshare.airline.common.auth.model.MenuDTO;
import com.codeshare.airline.common.utils.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = AuditMapper.class)
public interface MenuMapper extends GenericMapper<Menu, MenuDTO> {

}
