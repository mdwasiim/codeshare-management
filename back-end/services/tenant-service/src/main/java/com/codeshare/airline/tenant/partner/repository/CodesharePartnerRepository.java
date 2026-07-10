package com.codeshare.airline.tenant.partner.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.tenant.partner.entities.CodesharePartner;

import java.util.UUID;

public interface CodesharePartnerRepository extends CSMDataBaseRepository<CodesharePartner, UUID> {

    boolean existsByTenantIdAndHomeAirlineIdAndPartnerAirlineId(UUID tenantId, UUID homeAirlineId, UUID partnerAirlineId);
}
