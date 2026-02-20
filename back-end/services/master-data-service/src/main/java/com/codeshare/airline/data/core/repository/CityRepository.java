package com.codeshare.airline.data.core.repository;

import com.codeshare.airline.data.core.eitities.City;
import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CityRepository
        extends CSMDataBaseRepository<City, UUID> {

    List<City> findByCountryId(UUID countryId);

    List<City> findByStateId(UUID stateId);

    @Query("""
        SELECT c FROM City c
        WHERE LOWER(c.cityName) LIKE LOWER(CONCAT('%', :keyword, '%'))
           OR LOWER(c.iataCityCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
        """)
    Page<City> search(@Param("keyword") String keyword, Pageable pageable);

    @Query("""
        SELECT c FROM City c
        WHERE LOWER(c.cityName) LIKE LOWER(CONCAT(:keyword, '%'))
        ORDER BY c.cityName ASC
        """)
    List<City> autocomplete(@Param("keyword") String keyword);

    @Query("""
    SELECT c FROM City c
    WHERE LOWER(c.cityName) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(c.iataCityCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
""")
    List<City> searchForLocation(@Param("keyword") String keyword);

    Optional<City> findByCityName(String cityName);
}