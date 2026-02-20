package com.codeshare.airline.data.core.controller;

import com.codeshare.airline.core.dto.georegion.CityDTO;
import com.codeshare.airline.data.common.BaseController;
import com.codeshare.airline.data.core.service.CityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/cities")
public class CityController
        extends BaseController<CityDTO, UUID> {

    private final CityService service;
    public CityController(CityService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/country/{countryId}")
    public List<CityDTO> getByCountry(@PathVariable UUID countryId) {
        return service.getByCountry(countryId);
    }

    @GetMapping("/state/{stateId}")
    public List<CityDTO> getByState(@PathVariable UUID stateId) {
        return service.getByState(stateId);
    }

    @GetMapping("/search")
    public Page<CityDTO> search(
            @RequestParam String keyword,
            Pageable pageable) {
        return service.search(keyword, pageable);
    }

    @GetMapping("/autocomplete")
    public List<CityDTO> autocomplete(
            @RequestParam String keyword) {
        return service.autocomplete(keyword);
    }
}