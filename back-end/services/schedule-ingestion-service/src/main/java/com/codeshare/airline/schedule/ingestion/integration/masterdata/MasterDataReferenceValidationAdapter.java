package com.codeshare.airline.schedule.ingestion.integration.masterdata;

import com.codeshare.airline.platform.core.dto.master.validation.ScheduleCodeListValidationRequestDTO;
import com.codeshare.airline.platform.core.dto.master.validation.ScheduleCodeListValidationResponseDTO;
import com.codeshare.airline.schedule.ingestion.application.validation.MasterDataReferenceValidationPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
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
                validateSingleCode(request -> request.setAirlineCodes(List.of(key))));
    }

    @Override
    public boolean airportExists(String airportCode) {
        String normalized = normalize(airportCode);
        if (normalized.isBlank()) {
            return false;
        }

        return airportCache.computeIfAbsent(normalized, key ->
                validateSingleCode(request -> request.setAirportCodes(List.of(key))));
    }

    @Override
    public boolean aircraftExists(String aircraftCode) {
        String normalized = normalize(aircraftCode);
        if (normalized.isBlank()) {
            return false;
        }

        return aircraftCache.computeIfAbsent(normalized, key ->
                validateSingleCode(request -> request.setAircraftTypeCodes(List.of(key))));
    }

    @Override
    public boolean serviceTypeExists(String serviceTypeCode) {
        String normalized = normalize(serviceTypeCode);
        if (normalized.isBlank()) {
            return false;
        }

        return serviceTypeCache.computeIfAbsent(normalized, key ->
                validateSingleCode(request -> request.setServiceTypeCodes(List.of(key))));
    }

    @Override
    public boolean trafficRestrictionExists(String trafficRestrictionCode) {
        String normalized = normalize(trafficRestrictionCode);
        if (normalized.isBlank()) {
            return false;
        }

        return trafficRestrictionCache.computeIfAbsent(normalized, key ->
                validateSingleCode(request -> request.setTrafficRestrictionCodes(List.of(key))));
    }

    private boolean validateSingleCode(Consumer<ScheduleCodeListValidationRequestDTO> requestConfigurer) {
        ScheduleCodeListValidationRequestDTO request = new ScheduleCodeListValidationRequestDTO();
        requestConfigurer.accept(request);
        ScheduleCodeListValidationResponseDTO response = client.validateScheduleCodeLists(request);
        return response != null && response.isValid();
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toUpperCase(Locale.ENGLISH);
    }
}
