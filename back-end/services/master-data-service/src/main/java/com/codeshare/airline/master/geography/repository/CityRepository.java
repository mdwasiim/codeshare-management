package com.codeshare.airline.master.geography.repository;

import com.codeshare.airline.master.geography.entities.City;
import com.codeshare.airline.platform.data.jpa.repository.CSMDataBaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CityRepository
        extends CSMDataBaseRepository<City, Long> {

    List<City> findByCountryId(Long countryId);

    List<City> findByStateId(Long stateId);

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