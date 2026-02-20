package com.codeshare.airline.data.core.controller;

import com.codeshare.airline.core.dto.georegion.AirportDTO;
import com.codeshare.airline.data.common.BaseController;
import com.codeshare.airline.data.core.service.AirportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/airports")
public class AirportController
        extends BaseController<AirportDTO, UUID> {

    private final AirportService service;

    public AirportController(AirportService service) {
        super(service);
        this.service = service;
    }


    @GetMapping("/iata/{iata}")
    public AirportDTO getByIata(@PathVariable String iata) {
        return service.getByIata(iata);
    }

    @GetMapping("/icao/{icao}")
    public AirportDTO getByIcao(@PathVariable String icao) {
        return service.getByIcao(icao);
    }

    @GetMapping("/country/{countryId}")
    public List<AirportDTO> getByCountry(@PathVariable UUID countryId) {
        return service.getByCountry(countryId);
    }

    @GetMapping("/city/{cityId}")
    public List<AirportDTO> getByCity(@PathVariable UUID cityId) {
        return service.getByCity(cityId);
    }

    @GetMapping("/hub")
    public List<AirportDTO> getHubAirports() {
        return service.getHubAirports();
    }

    @GetMapping("/international")
    public List<AirportDTO> getInternationalAirports() {
        return service.getInternationalAirports();
    }

    @GetMapping("/search")
    public Page<AirportDTO> search(
            @RequestParam String keyword,
            Pageable pageable) {
        return service.search(keyword, pageable);
    }

    @GetMapping("/nearby")
    public List<AirportDTO> findNearby(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam double radiusKm) {

        return service.findNearby(latitude, longitude, radiusKm);
    }
}