package com.codeshare.airline.tenant.repository.partner;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.tenant.entities.partner.CodesharePartnerProfile;

import java.util.UUID;

public interface CodesharePartnerProfileRepository extends CSMDataBaseRepository<CodesharePartnerProfile, UUID> {

    boolean existsByPartner_IdAndProfileCode(UUID partnerId, String profileCode);
}
