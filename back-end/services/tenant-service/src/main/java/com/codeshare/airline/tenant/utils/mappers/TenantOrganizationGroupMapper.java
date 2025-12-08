package com.codeshare.airline.tenant.utils.mappers;

import com.codeshare.airline.common.services.mapper.GenericMapper;
import com.codeshare.airline.common.tenant.model.TenantOrganizationGroupDTO;
import com.codeshare.airline.tenant.entities.Tenant;
import com.codeshare.airline.tenant.entities.TenantOrganization;
import com.codeshare.airline.tenant.entities.TenantOrganizationGroup;
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
public interface TenantOrganizationGroupMapper extends GenericMapper<TenantOrganizationGroup, TenantOrganizationGroupDTO> {

    // ENTITY → DTO
    @Override
    @Mapping(source = "tenant.id", target = "tenantId")
    @Mapping(source = "tenantOrganization.id", target = "organizationId")
    TenantOrganizationGroupDTO toDTO(TenantOrganizationGroup entity);

    // DTO → ENTITY (Create)
    @Override
    @Mapping(target = "tenant", expression = "java(toTenant(dto.getTenantId()))")
    @Mapping(target = "tenantOrganization", expression = "java(toOrg(dto.getOrganizationId()))")
    @Mapping(target = "id", ignore = true)
    TenantOrganizationGroup toEntity(TenantOrganizationGroupDTO dto);

    // DTO → ENTITY (Update)
    @Override
    @Mapping(target = "tenant", expression = "java(toTenant(dto.getTenantId()))")
    @Mapping(target = "tenantOrganization", expression = "java(toOrg(dto.getOrganizationId()))")
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(TenantOrganizationGroupDTO dto, @MappingTarget TenantOrganizationGroup entity);

    // Helpers
    default Tenant toTenant(UUID id) {
        if (id == null) return null;
        Tenant t = new Tenant();
        t.setId(id);
        return t;
    }

    default TenantOrganization toOrg(UUID id) {
        if (id == null) return null;
        TenantOrganization org = new TenantOrganization();
        org.setId(id);
        return org;
    }
}
