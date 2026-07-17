package com.codeshare.airline.schedule.ingestion.integration.masterdata;

import com.codeshare.airline.platform.core.dto.master.aircraft.AircraftTypeDTO;
import com.codeshare.airline.platform.core.dto.master.airline.AirlineCarrierDTO;
import com.codeshare.airline.platform.core.dto.master.flightcommercial.passenger.ServiceTypeDTO;
import com.codeshare.airline.platform.core.dto.master.flightcommercial.schedule.TrafficRestrictionCodeDTO;
import com.codeshare.airline.platform.core.dto.master.georegion.AirportDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "master-data-reference-client",
        url = "${master-data.service.url:http://localhost:8089}"
)
public interface MasterDataReferenceClient {

    @GetMapping("/airline-carriers")
    List<AirlineCarrierDTO> getAirlineCarriers();

    @GetMapping("/airports/iata/{iata}")
    AirportDTO getAirportByIata(@PathVariable("iata") String iata);

    @GetMapping("/aircraft-types")
    List<AircraftTypeDTO> getAircraftTypes();

    @GetMapping("/flight-commercial-service-types")
    List<ServiceTypeDTO> getServiceTypes();

    @GetMapping("/traffic-restriction-codes")
    List<TrafficRestrictionCodeDTO> getTrafficRestrictionCodes();
}
