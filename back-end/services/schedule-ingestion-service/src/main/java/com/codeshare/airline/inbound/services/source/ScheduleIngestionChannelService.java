package com.codeshare.airline.inbound.services.source;

import com.codeshare.airline.inbound.dto.source.AirlineIngestionProfileDTO;

import java.util.UUID;

public interface ScheduleIngestionChannelService {

    AirlineIngestionProfileDTO createProfile(AirlineIngestionProfileDTO dto);

    AirlineIngestionProfileDTO updateProfile(UUID id, AirlineIngestionProfileDTO dto);

    AirlineIngestionProfileDTO getProfile(String airlineCode);

    void deleteProfile(UUID id);

    void enableProfile(UUID id, boolean enabled);
}