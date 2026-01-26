package com.codeshare.airline.auth.utils.mappers;

import com.codeshare.airline.auth.entities.Menu;
import com.codeshare.airline.core.dto.tenant.MenuDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.Comparator;

@Mapper(componentModel = "spring",config = CSMGenericMapper.class,  unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuMapper extends CSMGenericMapper<Menu, MenuDTO> {

    @Override
    MenuDTO toDTO(Menu menu);

    @AfterMapping
    default void enrich(Menu menu, @MappingTarget MenuDTO dto) {

        // ✅ parentId mapping
        if (menu.getParentMenu() != null) {
            dto.setParentId(menu.getParentMenu().getId());
        }

        // ✅ tenantId mapping
        if (menu.getTenant() != null) {
            dto.setTenantId(menu.getTenant().getId());
        }

        // ✅ recursive items mapping
        if (menu.getItems() != null && !menu.getItems().isEmpty()) {
            dto.setItems(
                    menu.getItems().stream()
                            .sorted(Comparator.comparing(Menu::getDisplayOrder,
                                    Comparator.nullsLast(Integer::compareTo)))
                            .map(this::toDTO)
                            .toList()
            );
        }
    }

}
