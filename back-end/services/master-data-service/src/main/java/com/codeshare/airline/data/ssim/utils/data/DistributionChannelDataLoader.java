package com.codeshare.airline.data.ssim.utils.data;

import com.codeshare.airline.core.enums.common.Status;
import com.codeshare.airline.data.ssim.eitities.DistributionChannel;
import com.codeshare.airline.data.ssim.repository.DistributionChannelRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DistributionChannelDataLoader {

    private final DistributionChannelRepository repository;

    @PostConstruct
    public void load() {

        if (repository.count() > 0) return;

        save("1A", "Amadeus GDS", "GDS", "EDIFACT",
                "https://amadeus-endpoint");

        save("1G", "Galileo GDS", "GDS", "EDIFACT",
                "https://galileo-endpoint");

        save("SITA", "SITA Messaging", "SITA", "MQ",
                "mq://sita-queue");

        save("API", "Internal API Distribution", "API", "REST",
                "https://internal-api");
    }

    private void save(String code,
                      String name,
                      String type,
                      String protocol,
                      String endpoint) {

        DistributionChannel entity = new DistributionChannel();
        entity.setChannelCode(code);
        entity.setChannelName(name);
        entity.setChannelType(type);
        entity.setProtocolType(protocol);
        entity.setEndpointUrl(endpoint);
        entity.setAutoSend(true);
        entity.setStatusCode(Status.ACTIVE);
        entity.setEffectiveFrom(LocalDate.now());

        repository.save(entity);
    }
}