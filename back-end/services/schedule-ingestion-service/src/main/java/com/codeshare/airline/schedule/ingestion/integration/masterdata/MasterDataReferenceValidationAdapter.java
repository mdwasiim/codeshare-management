package com.codeshare.airline.schedule.ingestion.integration.masterdata;

import com.codeshare.airline.platform.core.dto.master.aircraft.AircraftTypeDTO;
import com.codeshare.airline.platform.core.dto.master.airline.AirlineCarrierDTO;
import com.codeshare.airline.platform.core.dto.master.flightcommercial.passenger.ServiceTypeDTO;
import com.codeshare.airline.platform.core.dto.master.flightcommercial.schedule.TrafficRestrictionCodeDTO;
import com.codeshare.airline.schedule.ingestion.application.validation.MasterDataReferenceValidationPort;
import feign.FeignException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class MasterDataReferenceValidationAdapter implements MasterDataReferenceValidationPort {

    private final MasterDataReferenceClient client;
    private final ConcurrentMap<String, Boolean> airportCache = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Boolean> airlineCache = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Boolean> aircraftCache = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Boolean> serviceTypeCache = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, Boolean> trafficRestrictionCache = new ConcurrentHashMap<>();

    public MasterDataReferenceValidationAdapter(MasterDataReferenceClient client) {
        this.client = client;
    }

    @Override
    public boolean airlineExists(String airlineCode) {
        String normalized = normalize(airlineCode);
        if (normalized.isBlank()) {
            return false;
        }

        return airlineCache.computeIfAbsent(normalized, key ->
                safeList(client.getAirlineCarriers()).stream()
                        .anyMatch(carrier -> key.equals(normalize(carrier.getIataCode()))
                                || key.equals(normalize(carrier.getIcaoCode()))));
    }

    @Override
    public boolean airportExists(String airportCode) {
        String normalized = normalize(airportCode);
        if (normalized.isBlank()) {
            return false;
        }

        return airportCache.computeIfAbsent(normalized, key -> {
            try {
                return client.getAirportByIata(key) != null;
            } catch (FeignException.NotFound ex) {
                return false;
            }
        });
    }

    @Override
    public boolean aircraftExists(String aircraftCode) {
        String normalized = normalize(aircraftCode);
        if (normalized.isBlank()) {
            return false;
        }

        return aircraftCache.computeIfAbsent(normalized, key ->
                safeList(client.getAircraftTypes()).stream()
                        .anyMatch(type -> key.equals(normalize(type.getIataCode()))
                                || key.equals(normalize(type.getIcaoCode()))
                                || key.equals(normalize(type.getModelCode()))));
    }

    @Override
    public boolean serviceTypeExists(String serviceTypeCode) {
        String normalized = normalize(serviceTypeCode);
        if (normalized.isBlank()) {
            return false;
        }

        return serviceTypeCache.computeIfAbsent(normalized, key ->
                safeList(client.getServiceTypes()).stream()
                        .anyMatch(type -> key.equals(normalize(type.getServiceTypeCode()))));
    }

    @Override
    public boolean trafficRestrictionExists(String trafficRestrictionCode) {
        String normalized = normalize(trafficRestrictionCode);
        if (normalized.isBlank()) {
            return false;
        }

        return trafficRestrictionCache.computeIfAbsent(normalized, key ->
                safeList(client.getTrafficRestrictionCodes()).stream()
                        .anyMatch(code -> key.equals(normalize(code.getRestrictionCode()))));
    }

    private <T> List<T> safeList(List<T> values) {
        return values == null ? List.of() : values;
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toUpperCase(Locale.ENGLISH);
    }
}
