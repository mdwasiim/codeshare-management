package com.codeshare.airline.schedule.source.persistence.service;

import com.codeshare.airline.schedule.source.camel.lifecycle.RouteRefreshService;
import com.codeshare.airline.schedule.source.persistence.dto.AirlineIngestionChannelDTO;
import com.codeshare.airline.schedule.source.persistence.dto.AirlineIngestionProfileDTO;
import com.codeshare.airline.schedule.source.persistence.entity.AirlineIngestionChannel;
import com.codeshare.airline.schedule.source.persistence.entity.AirlineIngestionProfile;
import com.codeshare.airline.schedule.source.persistence.mapper.AirlineIngestionMapper;
import com.codeshare.airline.schedule.source.persistence.repository.AirlineIngestionProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class DefaultAirlineIngestionService implements AirlineIngestionService {

    private final AirlineIngestionProfileRepository profileRepository;
    private final RouteRefreshService routeRefreshService;

    @Override
    public AirlineIngestionProfileDTO createProfile(AirlineIngestionProfileDTO dto) {

        validateBusinessRules(dto);

        AirlineIngestionProfile entity =
                AirlineIngestionMapper.toEntity(dto);

        AirlineIngestionProfile saved =
                profileRepository.save(entity);

        try {
            routeRefreshService.refreshRoutes();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return mapToDto(saved);
    }

    @Override
    public AirlineIngestionProfileDTO updateProfile(UUID id,
                                                    AirlineIngestionProfileDTO dto) {

        AirlineIngestionProfile existing =
                profileRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        existing.getChannels().clear();

        dto.getChannels().forEach(channelDTO -> {
            AirlineIngestionChannel channel =
                    AirlineIngestionMapper.toChannelEntity(channelDTO);
            existing.addChannel(channel);
        });

        existing.setEnabled(dto.getEnabled());
        existing.setPollIntervalMs(dto.getPollIntervalMs());

        AirlineIngestionProfileDTO airlineIngestionProfileDTO = mapToDto(profileRepository.save(existing));
        try {
            routeRefreshService.refreshRoutes();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return airlineIngestionProfileDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public AirlineIngestionProfileDTO getProfile(String airlineCode) {

        AirlineIngestionProfile profile =
                profileRepository.findByAirlineCode(airlineCode)
                        .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        return mapToDto(profile);
    }

    @Override
    public void deleteProfile(UUID id) {
        profileRepository.deleteById(id);
    }

    @Override
    public void enableProfile(UUID id, boolean enabled) {
        AirlineIngestionProfile profile =
                profileRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        profile.setEnabled(enabled);
    }

    /* ===============================
       BUSINESS VALIDATION
       =============================== */

    private void validateBusinessRules(AirlineIngestionProfileDTO dto) {

        if (dto.getChannels() == null || dto.getChannels().isEmpty()) {
            throw new IllegalArgumentException("At least one channel required");
        }

        dto.getChannels().forEach(channel -> {

            switch (channel.getSourceType()) {

                case SFTP -> {
                    require(channel.getHost(), "SFTP host required");
                    require(channel.getPort(), "SFTP port required");
                }

                case MQ -> {
                    require(channel.getBrokerUrl(), "MQ brokerUrl required");
                }

                case EMAIL -> {
                    require(channel.getProtocol(), "Email protocol required");
                }
            }
        });
    }

    private void require(Object value, String message) {
        if (value == null ||
                (value instanceof String s && s.isBlank())) {
            throw new IllegalArgumentException(message);
        }
    }

    private AirlineIngestionProfileDTO mapToDto(AirlineIngestionProfile entity) {

        AirlineIngestionProfileDTO dto =
                new AirlineIngestionProfileDTO();

        dto.setId(entity.getId());
        dto.setAirlineCode(entity.getAirlineCode());
        dto.setSourceSystem(entity.getSourceSystem());
        dto.setEnabled(entity.getEnabled());
        dto.setPollIntervalMs(entity.getPollIntervalMs());

        dto.setChannels(
                entity.getChannels()
                        .stream()
                        .map(channel -> {
                            AirlineIngestionChannelDTO c =
                                    new AirlineIngestionChannelDTO();
                            c.setId(channel.getId());
                            c.setMessageType(channel.getMessageType());
                            c.setSourceType(channel.getSourceType());
                            c.setHost(channel.getHost());
                            c.setPort(channel.getPort());
                            c.setBrokerUrl(channel.getBrokerUrl());
                            c.setQueueName(channel.getQueueName());
                            return c;
                        })
                        .toList()
        );

        return dto;
    }
}