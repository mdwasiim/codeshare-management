package com.codeshare.airline.tenant.utils.mappers;

import com.codeshare.airline.common.services.mapper.GenericMapper;
import com.codeshare.airline.common.tenant.model.TenantDTO;
import com.codeshare.airline.tenant.entities.Tenant;
import com.codeshare.airline.tenant.entities.TenantDataSource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.UUID;

@Mapper(
        componentModel = "spring",
        config = GenericMapper.class
)
public interface TenantMapper extends GenericMapper<Tenant, TenantDTO> {

    @Override
    @Mapping(source = "tenantDataSource.id", target = "dataSourceId")
    TenantDTO toDTO(Tenant entity);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantOrganizations", ignore = true)
    @Mapping(target = "tenantDataSource", expression = "java(toDataSource(dto.getDataSourceId()))")
    Tenant toEntity(TenantDTO dto);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tenantOrganizations", ignore = true)
    @Mapping(target = "tenantDataSource", expression = "java(toDataSource(dto.getDataSourceId()))")
    void updateEntityFromDto(TenantDTO dto, @MappingTarget Tenant entity);

    default TenantDataSource toDataSource(UUID id) {
        if (id == null) return null;
        TenantDataSource ds = new TenantDataSource();
        ds.setId(id);
        return ds;
    }
}

