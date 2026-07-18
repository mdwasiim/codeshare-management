package com.codeshare.airline.distribution.engine.integration.adapter;

import com.codeshare.airline.platform.core.dto.master.codesharepartner.CodesharePartnerCommunicationProfileDTO;
import com.codeshare.airline.platform.core.dto.master.codesharepartner.CodesharePartnerDistributionProfileDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.OutboundScheduleMessageDTO;
import com.codeshare.airline.platform.core.enums.master.codesharepartner.CommunicationProtocol;
import com.codeshare.airline.platform.core.events.schedule.DistributionRequestedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class KafkaDistributionAdapter implements DistributionAdapter {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaDistributionAdapter(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public CommunicationProtocol supports() {
        return CommunicationProtocol.KAFKA;
    }

    @Override
    public void dispatch(
            DistributionRequestedEvent event,
            CodesharePartnerDistributionProfileDTO profile,
            CodesharePartnerCommunicationProfileDTO communicationProfile,
            OutboundScheduleMessageDTO outboundMessage
    ) {
        String topic = requireEndpoint(communicationProfile);
        try {
            kafkaTemplate.send(topic, outboundMessage.getOutboundMessageId().toString(), outboundMessage.getPayload())
                    .get(30, TimeUnit.SECONDS);
        } catch (Exception ex) {
            throw new IllegalStateException("KAFKA distribution dispatch failed for topic=" + topic, ex);
        }
        log.info(
                "Distribution adapter KAFKA dispatched topic={} changeSetId={} outboundMessageId={} partner={} profile={} payloadLength={}",
                topic,
                event.getChangeSetId(),
                outboundMessage.getOutboundMessageId(),
                event.getPartnerCode(),
                profile.getProfileCode(),
                outboundMessage.getPayload() != null ? outboundMessage.getPayload().length() : 0
        );
    }

    private String requireEndpoint(CodesharePartnerCommunicationProfileDTO communicationProfile) {
        if (communicationProfile == null || communicationProfile.getEndpointUrl() == null || communicationProfile.getEndpointUrl().isBlank()) {
            throw new IllegalStateException("KAFKA communication profile endpointUrl topic is required");
        }
        return communicationProfile.getEndpointUrl().trim();
    }
}
