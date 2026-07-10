package com.codeshare.airline.tenant.mappers;

import com.codeshare.airline.core.dto.tenant.TenantDTO;
import com.codeshare.airline.core.mapper.CSMGenericMapper;
import com.codeshare.airline.core.mapper.CSMMapperConfig;
import com.codeshare.airline.tenant.entities.Tenant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = CSMMapperConfig.class)
public interface TenantMapper extends CSMGenericMapper<Tenant, TenantDTO> {

    @Override
    TenantDTO toDTO(Tenant entity);

    @Override
    Tenant toEntity(TenantDTO dto);
}
