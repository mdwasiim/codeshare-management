package com.codeshare.airline.master.airlines.controller;

import com.codeshare.airline.platform.core.dto.master.airline.AirlineCarrierDTO;
import com.codeshare.airline.master.airlines.mappers.AirlineCarrierMapper;
import com.codeshare.airline.master.airlines.repository.AirlineCarrierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/internal/airline-carriers")
@RequiredArgsConstructor
public class InternalAirlineCarrierController {

    private final AirlineCarrierRepository repository;
    private final AirlineCarrierMapper mapper;

    @GetMapping("/iata/{iataCode}")
    public AirlineCarrierDTO getByIataCode(@PathVariable String iataCode) {
        return repository.findByIataCode(iataCode.toUpperCase())
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Airline not found: " + iataCode));
    }
}
