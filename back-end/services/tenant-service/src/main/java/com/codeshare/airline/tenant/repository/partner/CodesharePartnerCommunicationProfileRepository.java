package com.codeshare.airline.tenant.repository.partner;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.tenant.entities.partner.CodesharePartnerCommunicationProfile;

import java.util.UUID;

public interface CodesharePartnerCommunicationProfileRepository extends CSMDataBaseRepository<CodesharePartnerCommunicationProfile, UUID> {

    boolean existsByPartner_IdAndProfileCode(UUID partnerId, String profileCode);
}
