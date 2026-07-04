package com.codeshare.airline.master.airlines.codesharepartner.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.airlines.codesharepartner.entities.CodesharePartnerDistributionProfile;

import java.util.UUID;

public interface CodesharePartnerDistributionProfileRepository extends CSMDataBaseRepository<CodesharePartnerDistributionProfile, UUID> {
    boolean existsByPartner_HomeAirline_IataCodeAndPartner_PartnerAirline_IataCodeAndProfileCode(
            String homeIataCode,
            String partnerIataCode,
            String profileCode
    );
}
