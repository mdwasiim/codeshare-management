package com.codeshare.airline.master.geography.controller;

import com.codeshare.airline.core.dto.master.georegion.LocationSearchDTO;
import com.codeshare.airline.master.geography.service.LocationSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationSearchController {

    private final LocationSearchService service;

    @GetMapping("/search")
    public List<LocationSearchDTO> search(
            @RequestParam String keyword) {
        return service.search(keyword);
    }
}