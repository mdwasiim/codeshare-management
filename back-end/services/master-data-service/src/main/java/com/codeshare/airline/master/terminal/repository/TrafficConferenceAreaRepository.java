package com.codeshare.airline.master.terminal.repository;

import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import com.codeshare.airline.master.terminal.entities.TrafficConferenceArea;

import java.util.Optional;

public interface TrafficConferenceAreaRepository extends CSMDataBaseRepository<TrafficConferenceArea, Long> {

    Optional<TrafficConferenceArea> findByAreaCode(String areaCode);

    boolean existsByAreaCode(String areaCode);
}
