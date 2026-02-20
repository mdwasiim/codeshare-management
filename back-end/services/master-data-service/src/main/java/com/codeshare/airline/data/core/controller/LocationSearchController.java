package com.codeshare.airline.data.core.controller;

import com.codeshare.airline.core.dto.georegion.LocationSearchDTO;
import com.codeshare.airline.data.core.service.LocationSearchService;
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