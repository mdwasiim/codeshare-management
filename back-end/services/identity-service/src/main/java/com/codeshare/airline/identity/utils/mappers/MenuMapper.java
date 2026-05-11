package com.codeshare.airline.identity.utils.mappers;

import com.codeshare.airline.core.dto.tenant.MenuDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.identity.entities.Menu;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CSMMapperConfig.class)
public interface MenuMapper extends CSMGenericMapper<Menu, MenuDTO> {

    @Override
    MenuDTO toDTO(Menu menu);

    // 🔥 ADD THIS
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenant", ignore = true)
    @Mapping(target = "parentMenu", ignore = true)
    void updateEntityFromDto(MenuDTO dto, @MappingTarget Menu entity);

    @AfterMapping
    default void enrich(Menu menu, @MappingTarget MenuDTO.MenuDTOBuilder<?, ?> dto) {
        if (menu == null || dto == null) return;

        if (menu.getParentMenu() != null) {
            dto.parentId(menu.getParentMenu().getId());
        }

        if (menu.getTenant() != null) {
            dto.tenantId(menu.getTenant().getId());
        }
    }
}
