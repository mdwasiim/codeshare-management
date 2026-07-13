package com.codeshare.airline.master.geography.controller;

import com.codeshare.airline.platform.core.dto.master.georegion.CityDTO;
import com.codeshare.airline.master.geography.service.CityService;
import com.codeshare.airline.master.common.base.BaseController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cities")
public class CityController
        extends BaseController<CityDTO, Long> {

    private final CityService service;
    public CityController(CityService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/country/{countryId}")
    public List<CityDTO> getByCountry(@PathVariable Long countryId) {
        return service.getByCountry(countryId);
    }

    @GetMapping("/state/{stateId}")
    public List<CityDTO> getByState(@PathVariable Long stateId) {
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