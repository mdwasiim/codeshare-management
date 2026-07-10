package com.codeshare.airline.tenant.repository.partner;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.tenant.entities.partner.CodesharePartnerDistributionProfile;

import java.util.UUID;

public interface CodesharePartnerDistributionProfileRepository extends CSMDataBaseRepository<CodesharePartnerDistributionProfile, UUID> {

    boolean existsByPartner_IdAndProfileCode(UUID partnerId, String profileCode);
}
