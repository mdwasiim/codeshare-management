package com.codeshare.airline.tenant.repository.partner;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.tenant.entities.partner.CodesharePartner;

import java.util.UUID;

public interface CodesharePartnerRepository extends CSMDataBaseRepository<CodesharePartner, UUID> {

    boolean existsByTenantIdAndHomeAirlineIdAndPartnerAirlineId(UUID tenantId, UUID homeAirlineId, UUID partnerAirlineId);
}
