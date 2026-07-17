package com.codeshare.airline.tenant.repository.partner;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.tenant.entities.partner.CodesharePartnerProfile;

import java.util.Optional;

public interface CodesharePartnerProfileRepository extends CSMDataBaseRepository<CodesharePartnerProfile, Long> {

    boolean existsByPartner_IdAndProfileCode(Long partnerId, String profileCode);

    Optional<CodesharePartnerProfile> findByPartner_IdAndProfileCode(Long partnerId, String profileCode);
}
