package com.codeshare.airline.tenant.repository.partner;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.tenant.entities.partner.CodesharePartnerDistributionProfile;


public interface CodesharePartnerDistributionProfileRepository extends CSMDataBaseRepository<CodesharePartnerDistributionProfile, Long> {

    boolean existsByPartner_IdAndProfileCode(Long partnerId, String profileCode);
}
