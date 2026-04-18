package com.codeshare.airline.master.airport.georegion.controller;

import com.codeshare.airline.dto.airport.georegion.AirlineCarrierDTO;
import com.codeshare.airline.master.airport.georegion.service.AirlineCarrierService;
import com.codeshare.airline.master.common.base.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/airlines")
public class AirlineCarrierController
        extends BaseController<AirlineCarrierDTO, UUID> {

    private final AirlineCarrierService service;

    public AirlineCarrierController(AirlineCarrierService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/iata/{iata}")
    public AirlineCarrierDTO getByIata(@PathVariable String iata) {
        return service.getByIata(iata);
    }

    @GetMapping("/icao/{icao}")
    public AirlineCarrierDTO getByIcao(@PathVariable String icao) {
        return service.getByIcao(icao);
    }

    @GetMapping("/country/{countryId}")
    public List<AirlineCarrierDTO> getByCountry(@PathVariable UUID countryId) {
        return service.getByCountry(countryId);
    }
}