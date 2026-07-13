package com.codeshare.airline.tenant.repository.partner;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.tenant.entities.partner.CodesharePartnerCommunicationProfile;

import java.util.Optional;

public interface CodesharePartnerCommunicationProfileRepository extends CSMDataBaseRepository<CodesharePartnerCommunicationProfile, Long> {

    boolean existsByPartner_IdAndProfileCode(Long partnerId, String profileCode);

    Optional<CodesharePartnerCommunicationProfile> findByPartner_IdAndProfileCode(Long partnerId, String profileCode);
}
