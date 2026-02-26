package com.codeshare.airline.schedule.source.persistence.service;

import com.codeshare.airline.schedule.source.persistence.dto.AirlineIngestionProfileDTO;

import java.util.UUID;

public interface AirlineIngestionService {

    AirlineIngestionProfileDTO createProfile(AirlineIngestionProfileDTO dto);

    AirlineIngestionProfileDTO updateProfile(UUID id, AirlineIngestionProfileDTO dto);

    AirlineIngestionProfileDTO getProfile(String airlineCode);

    void deleteProfile(UUID id);

    void enableProfile(UUID id, boolean enabled);
}