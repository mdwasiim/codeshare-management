package com.codeshare.airline.master.airport.georegion.controller;

import com.codeshare.airline.dto.airport.georegion.LocationSearchDTO;
import com.codeshare.airline.master.airport.georegion.service.LocationSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationSearchController {

    private final LocationSearchService service;

    @GetMapping("/search")
    public List<LocationSearchDTO> search(
            @RequestParam String keyword) {
        return service.search(keyword);
    }
}