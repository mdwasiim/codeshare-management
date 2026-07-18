package com.codeshare.airline.distribution.engine.integration.adapter;

import com.codeshare.airline.platform.core.dto.master.codesharepartner.CodesharePartnerCommunicationProfileDTO;
import com.codeshare.airline.platform.core.dto.master.codesharepartner.CodesharePartnerDistributionProfileDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.OutboundScheduleMessageDTO;
import com.codeshare.airline.platform.core.enums.master.codesharepartner.CommunicationProtocol;
import com.codeshare.airline.platform.core.events.schedule.DistributionRequestedEvent;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MqDistributionAdapter implements DistributionAdapter {

    private final JmsTemplate jmsTemplate;

    public MqDistributionAdapter(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public CommunicationProtocol supports() {
        return CommunicationProtocol.MQ;
    }

    @Override
    public void dispatch(
            DistributionRequestedEvent event,
            CodesharePartnerDistributionProfileDTO profile,
            CodesharePartnerCommunicationProfileDTO communicationProfile,
            OutboundScheduleMessageDTO outboundMessage
    ) {
        String destination = requireEndpoint(communicationProfile);
        jmsTemplate.convertAndSend(destination, outboundMessage.getPayload(), message -> enrich(message, event, outboundMessage));
        log.info(
                "Distribution adapter MQ dispatched destination={} changeSetId={} outboundMessageId={} partner={} profile={} payloadLength={}",
                destination,
                event.getChangeSetId(),
                outboundMessage.getOutboundMessageId(),
                event.getPartnerCode(),
                profile.getProfileCode(),
                outboundMessage.getPayload() != null ? outboundMessage.getPayload().length() : 0
        );
    }

    private Message enrich(Message message, DistributionRequestedEvent event, OutboundScheduleMessageDTO outboundMessage) throws JMSException {
        message.setStringProperty("correlationId", event.getCorrelationId() != null ? event.getCorrelationId().toString() : null);
        message.setStringProperty("causationId", event.getCausationId() != null ? event.getCausationId().toString() : null);
        message.setStringProperty("outboundMessageId", outboundMessage.getOutboundMessageId().toString());
        message.setStringProperty("messageType", outboundMessage.getMessageType().name());
        return message;
    }

    private String requireEndpoint(CodesharePartnerCommunicationProfileDTO communicationProfile) {
        if (communicationProfile == null || communicationProfile.getEndpointUrl() == null || communicationProfile.getEndpointUrl().isBlank()) {
            throw new IllegalStateException("MQ communication profile endpointUrl is required");
        }
        return communicationProfile.getEndpointUrl().trim();
    }
}
