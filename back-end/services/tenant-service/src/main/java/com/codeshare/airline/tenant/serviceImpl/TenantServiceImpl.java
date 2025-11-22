package com.codeshare.airline.tenant.serviceImpl;

import com.codeshare.airline.common.tenant.model.TenantDTO;
import com.codeshare.airline.tenant.entities.Tenant;
import com.codeshare.airline.tenant.repository.TenantRepository;
import com.codeshare.airline.tenant.service.TenantService;
import com.codeshare.airline.tenant.utils.mappers.TenantMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TenantServiceImpl implements TenantService {

    private TenantRepository tenantRepository;
    private TenantMapper tenantMapper;

    @Autowired
    public TenantServiceImpl(TenantRepository tenantRepository, TenantMapper tenantMapper) {
        this.tenantRepository = tenantRepository;
        this.tenantMapper = tenantMapper;
    }

    @Override
    public TenantDTO createTenant(TenantDTO tenantDTO) {
        Tenant tenant = tenantMapper.toEntity(tenantDTO);
        tenantRepository.save(tenant);
        TenantDTO tenantResponse =  tenantMapper.toDTO(tenant);

        return tenantResponse;
    }
}
