package com.codeshare.airline.tenant.utils.mappers;

import com.codeshare.airline.common.services.mapper.GenericMapper;
import com.codeshare.airline.common.tenant.model.TenantOrganizationGroupUserDTO;
import com.codeshare.airline.tenant.entities.TenantOrganizationGroup;
import com.codeshare.airline.tenant.entities.TenantOrganizationGroupUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.UUID;

@Mapper(
        componentModel = "spring",
        config = GenericMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TenantOrganizationGroupUserMapper
        extends GenericMapper<TenantOrganizationGroupUser, TenantOrganizationGroupUserDTO> {

    // ENTITY → DTO
    @Override
    @Mapping(source = "group.id", target = "groupId")
    TenantOrganizationGroupUserDTO toDTO(TenantOrganizationGroupUser entity);

    // DTO → ENTITY
    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "group", expression = "java(toGroup(dto.getGroupId()))")
    TenantOrganizationGroupUser toEntity(TenantOrganizationGroupUserDTO dto);

    // DTO → ENTITY (Update)
    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "group", expression = "java(toGroup(dto.getGroupId()))")
    void updateEntityFromDto(TenantOrganizationGroupUserDTO dto, @MappingTarget TenantOrganizationGroupUser entity);

    // helper
    default TenantOrganizationGroup toGroup(UUID id) {
        if (id == null) return null;
        TenantOrganizationGroup g = new TenantOrganizationGroup();
        g.setId(id);
        return g;
    }
}
