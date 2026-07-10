package com.codeshare.airline.identity.access.identity.service.serviceImpl;

import com.codeshare.airline.core.exceptions.CSMResourceNotFoundException;
import com.codeshare.airline.identity.access.identity.entities.Tenant;
import com.codeshare.airline.identity.access.identity.repository.TenantRepository;
import com.codeshare.airline.identity.access.identity.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TenantServiceImpl implements TenantService {

    private final TenantRepository repository;

    @Override
    public Tenant getTenantByTenantCode(String tenantCode) {
        return repository.findByTenantCode(tenantCode)
                .orElseThrow(() -> new CSMResourceNotFoundException("Tenant not found: " + tenantCode));
    }
}
