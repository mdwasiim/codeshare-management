package com.codeshare.airline.master.airlines.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.airlines.entities.CodesharePartner;

import java.util.Optional;
import java.util.UUID;

public interface CodesharePartnerRepository extends CSMDataBaseRepository<CodesharePartner, UUID> {
    boolean existsByHomeAirline_IataCodeAndPartnerAirline_IataCode(String homeIataCode, String partnerIataCode);

    Optional<CodesharePartner> findByHomeAirline_IataCodeAndPartnerAirline_IataCode(String homeIataCode, String partnerIataCode);
}
