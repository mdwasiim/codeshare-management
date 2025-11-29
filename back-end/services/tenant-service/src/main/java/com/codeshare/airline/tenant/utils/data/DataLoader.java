package com.codeshare.airline.tenant.utils.data;

import com.codeshare.airline.tenant.repository.TenantDataSourceRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader {

    private final TenantLoader tenantLoader;
    private final DataSourceLoader dataSourceLoader;
    private final OrganizationLoader organizationLoader;
    private final TenantDataSourceRepository dataSourceRepo;

    @PostConstruct
    @Transactional
    public void load() {
        long count = dataSourceRepo.count();
        if(count==0){
            dataSourceLoader.loadDatasorce();
            tenantLoader.teanantLoader();
            organizationLoader.organizationLoad();
        }

    }
}
