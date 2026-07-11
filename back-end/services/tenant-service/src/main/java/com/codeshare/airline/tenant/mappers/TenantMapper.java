package com.codeshare.airline.tenant.mappers;

import com.codeshare.airline.platform.core.dto.tenant.TenantDTO;
import com.codeshare.airline.platform.core.mapper.CSMGenericMapper;
import com.codeshare.airline.platform.core.mapper.CSMMapperConfig;
import com.codeshare.airline.tenant.entities.Tenant;
import org.mapstruct.Mapper;
import org.mapstruct.ObjectFactory;

@Mapper(config = CSMMapperConfig.class)
public interface TenantMapper extends CSMGenericMapper<Tenant, TenantDTO> {

    @Override
    TenantDTO toDTO(Tenant entity);

    @Override
    Tenant toEntity(TenantDTO dto);

    @ObjectFactory
    default Tenant createTenant(TenantDTO dto) {
        return Tenant.builder().build();
    }
}
