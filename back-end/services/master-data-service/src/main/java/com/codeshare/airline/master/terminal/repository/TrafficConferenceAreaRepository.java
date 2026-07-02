package com.codeshare.airline.master.terminal.repository;

import com.codeshare.airline.data.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.terminal.entities.TrafficConferenceArea;

import java.util.Optional;
import java.util.UUID;

public interface TrafficConferenceAreaRepository extends CSMDataBaseRepository<TrafficConferenceArea, UUID> {

    Optional<TrafficConferenceArea> findByAreaCode(String areaCode);

    boolean existsByAreaCode(String areaCode);
}
