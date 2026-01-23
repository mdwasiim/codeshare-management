package com.codeshare.airline.auth.utils.mappers;

import com.codeshare.airline.auth.model.entities.Menu;
import com.codeshare.airline.core.dto.tenant.MenuDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",config = CSMGenericMapper.class,  unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuMapper extends CSMGenericMapper<Menu, MenuDTO> {

}
