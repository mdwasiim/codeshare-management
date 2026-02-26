package com.codeshare.airline.schedule.source.persistence.loader;

import com.codeshare.airline.schedule.domain.common.ScheduleMessageType;
import com.codeshare.airline.schedule.domain.common.ScheduleSourceType;
import com.codeshare.airline.schedule.source.persistence.entity.AirlineIngestionChannel;
import com.codeshare.airline.schedule.source.persistence.entity.AirlineIngestionProfile;
import com.codeshare.airline.schedule.source.persistence.repository.AirlineIngestionProfileRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DefaultIngestionDataInitializer {

    private final AirlineIngestionProfileRepository profileRepository;

    @PostConstruct
    @Transactional
    public void loadDefaults() {

        createAirlineIfMissing("QR");
        createAirlineIfMissing("EK");
    }

    private void createAirlineIfMissing(String airlineCode) {

        Optional<AirlineIngestionProfile> existing =
                profileRepository.findByAirlineCode(airlineCode);

        if (existing.isPresent()) {
            return; // Idempotent
        }

        AirlineIngestionProfile profile =
                new AirlineIngestionProfile();

        profile.setAirlineCode(airlineCode);
        profile.setSourceSystem("DEFAULT_SYSTEM");
        profile.setEnabled(true);
        profile.setPollIntervalMs(60000L);

        /* MQ for ASM + SSM */

        profile.addChannel(buildMqChannel(
                ScheduleMessageType.ASM,
                "tcp://localhost:61616",
                airlineCode+".ASM.IN"
        ));

        profile.addChannel(buildMqChannel(
                ScheduleMessageType.SSM,
                "tcp://localhost:61616",
                airlineCode+".SSM.IN"
        ));

        /* SFTP for SSIM */

        profile.addChannel(buildSftpChannel(
                ScheduleMessageType.SSIM,
                "sftp.airline.com",
                22,
                "ssim_user",
                "/inbound"
        ));

        profileRepository.save(profile);
    }

    private AirlineIngestionChannel buildMqChannel(
            ScheduleMessageType messageType,
            String brokerUrl,
            String queueName) {

        AirlineIngestionChannel channel =
                new AirlineIngestionChannel();

        channel.setMessageType(messageType);
        channel.setSourceType(ScheduleSourceType.MQ);
        channel.setBrokerUrl(brokerUrl);
        channel.setQueueName(queueName);
        channel.setConnectionTimeoutMs(10000);
        channel.setReadTimeoutMs(10000);

        return channel;
    }

    private AirlineIngestionChannel buildSftpChannel(
            ScheduleMessageType messageType,
            String host,
            int port,
            String username,
            String remoteDir) {

        AirlineIngestionChannel channel =
                new AirlineIngestionChannel();

        channel.setMessageType(messageType);
        channel.setSourceType(ScheduleSourceType.SFTP);
        channel.setHost(host);
        channel.setPort(port);
        channel.setUsername(username);
        channel.setRemoteDirectory(remoteDir);
        channel.setConnectionTimeoutMs(15000);
        channel.setReadTimeoutMs(15000);

        return channel;
    }
}