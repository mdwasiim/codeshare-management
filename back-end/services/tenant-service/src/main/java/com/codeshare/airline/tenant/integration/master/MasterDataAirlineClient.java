package com.codeshare.airline.tenant.integration.master;

import com.codeshare.airline.platform.core.dto.master.airline.AirlineCarrierDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "master-data-service",
        url = "${services.master-data.url:http://localhost:8084}"
)
public interface MasterDataAirlineClient {

    @GetMapping("/internal/airline-carriers/iata/{iataCode}")
    AirlineCarrierDTO getByIataCode(@PathVariable("iataCode") String iataCode);

    @GetMapping("/internal/airline-carriers/{id}")
    AirlineCarrierDTO getById(@PathVariable("id") Long id);
}
