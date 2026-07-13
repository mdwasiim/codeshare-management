package com.codeshare.airline.tenant.repository.partner;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.tenant.entities.partner.CodesharePartner;


public interface CodesharePartnerRepository extends CSMDataBaseRepository<CodesharePartner, Long> {

    boolean existsByTenantIdAndHomeAirlineIdAndPartnerAirlineId(Long tenantId, Long homeAirlineId, Long partnerAirlineId);
}
