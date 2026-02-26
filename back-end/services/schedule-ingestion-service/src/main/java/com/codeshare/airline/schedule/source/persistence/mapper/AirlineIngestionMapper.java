package com.codeshare.airline.schedule.source.persistence.mapper;


import com.codeshare.airline.schedule.source.persistence.dto.AirlineIngestionChannelDTO;
import com.codeshare.airline.schedule.source.persistence.dto.AirlineIngestionProfileDTO;
import com.codeshare.airline.schedule.source.persistence.entity.AirlineIngestionChannel;
import com.codeshare.airline.schedule.source.persistence.entity.AirlineIngestionProfile;

public class AirlineIngestionMapper {

    public static AirlineIngestionProfile toEntity(AirlineIngestionProfileDTO dto) {

        AirlineIngestionProfile profile = new AirlineIngestionProfile();
        profile.setAirlineCode(dto.getAirlineCode());
        profile.setSourceSystem(dto.getSourceSystem());
        profile.setEnabled(dto.getEnabled());
        profile.setPollIntervalMs(dto.getPollIntervalMs());

        if (dto.getChannels() != null) {
            dto.getChannels().forEach(channelDTO -> {
                AirlineIngestionChannel channel = toChannelEntity(channelDTO);
                profile.addChannel(channel);
            });
        }

        return profile;
    }

    public static AirlineIngestionChannel toChannelEntity(AirlineIngestionChannelDTO dto) {

        AirlineIngestionChannel channel = new AirlineIngestionChannel();

        channel.setMessageType(dto.getMessageType());
        channel.setSourceType(dto.getSourceType());
        channel.setHost(dto.getHost());
        channel.setPort(dto.getPort());
        channel.setUsername(dto.getUsername());
        channel.setPasswordEncrypted(dto.getPasswordEncrypted());
        channel.setRemoteDirectory(dto.getRemoteDirectory());
        channel.setProtocol(dto.getProtocol());
        channel.setMailbox(dto.getMailbox());
        channel.setBrokerUrl(dto.getBrokerUrl());
        channel.setQueueName(dto.getQueueName());
        channel.setTopicName(dto.getTopicName());
        channel.setConnectionTimeoutMs(dto.getConnectionTimeoutMs());
        channel.setReadTimeoutMs(dto.getReadTimeoutMs());

        return channel;
    }
}