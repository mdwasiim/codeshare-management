package com.codeshare.airline.data.core.repository;

import com.codeshare.airline.data.core.eitities.Airport;
import com.codeshare.airline.persistence.persistence.repository.CSMDataBaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AirportRepository
        extends CSMDataBaseRepository<Airport, UUID> {

    Optional<Airport> findByIataCode(String iataCode);

    Optional<Airport> findByIcaoCode(String icaoCode);

    List<Airport> findByCountryId(UUID countryId);

    List<Airport> findByCityId(UUID cityId);

    List<Airport> findByHubTrue();

    List<Airport> findByInternationalTrue();

    @Query("""
            SELECT a FROM Airport a
            WHERE LOWER(a.airportName) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(a.iataCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
               OR LOWER(a.icaoCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    Page<Airport> search(@Param("keyword") String keyword, Pageable pageable);


    @Query("""
    SELECT a FROM Airport a
    WHERE
    (6371 * acos(
        cos(radians(:latitude)) *
        cos(radians(a.latitude)) *
        cos(radians(a.longitude) - radians(:longitude)) +
        sin(radians(:latitude)) *
        sin(radians(a.latitude))
    )) <= :radius
""")
    List<Airport> findNearbyAirports(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("radius") double radius
    );

    @Query("""
    SELECT a FROM Airport a
    WHERE LOWER(a.airportName) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(a.iataCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
""")
    List<Airport> searchForLocation(@Param("keyword") String keyword);
}