package com.codeshare.airline.master.airlines.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.airlines.entities.AllianceMember;


public interface AllianceMemberRepository extends CSMDataBaseRepository<AllianceMember, Long> {
    boolean existsByAlliance_AllianceCodeAndAirline_IataCode(String allianceCode, String iataCode);
}
