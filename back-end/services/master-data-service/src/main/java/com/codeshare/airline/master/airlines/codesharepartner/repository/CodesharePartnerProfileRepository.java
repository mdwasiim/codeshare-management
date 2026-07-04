package com.codeshare.airline.master.airlines.codesharepartner.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.airlines.codesharepartner.entities.CodesharePartnerProfile;

import java.util.UUID;

public interface CodesharePartnerProfileRepository extends CSMDataBaseRepository<CodesharePartnerProfile, UUID> {
    boolean existsByPartner_HomeAirline_IataCodeAndPartner_PartnerAirline_IataCodeAndProfileCode(
            String homeIataCode,
            String partnerIataCode,
            String profileCode
    );
}
