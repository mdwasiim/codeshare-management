package com.codeshare.airline.distribution.engine.integration.adapter;

import com.codeshare.airline.platform.core.dto.master.codesharepartner.CodesharePartnerCommunicationProfileDTO;
import com.codeshare.airline.platform.core.dto.master.codesharepartner.CodesharePartnerDistributionProfileDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.OutboundScheduleMessageDTO;
import com.codeshare.airline.platform.core.enums.master.codesharepartner.CommunicationProtocol;
import com.codeshare.airline.platform.core.events.schedule.DistributionRequestedEvent;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
public class EmailDistributionAdapter implements DistributionAdapter {

    private final JavaMailSender mailSender;

    public EmailDistributionAdapter(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public CommunicationProtocol supports() {
        return CommunicationProtocol.EMAIL;
    }

    @Override
    public void dispatch(
            DistributionRequestedEvent event,
            CodesharePartnerDistributionProfileDTO profile,
            CodesharePartnerCommunicationProfileDTO communicationProfile,
            OutboundScheduleMessageDTO outboundMessage
    ) {
        String[] recipients = recipients(communicationProfile);
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(recipients);
        mail.setSubject(outboundMessage.getMessageType() + " " + outboundMessage.getAirlineCode() + " " + outboundMessage.getOutboundMessageId());
        mail.setText(outboundMessage.getPayload());
        mailSender.send(mail);
        log.info(
                "Distribution adapter EMAIL dispatched recipients={} changeSetId={} outboundMessageId={} partner={} profile={} payloadLength={}",
                Arrays.toString(recipients),
                event.getChangeSetId(),
                outboundMessage.getOutboundMessageId(),
                event.getPartnerCode(),
                profile.getProfileCode(),
                outboundMessage.getPayload() != null ? outboundMessage.getPayload().length() : 0
        );
    }

    private String[] recipients(CodesharePartnerCommunicationProfileDTO communicationProfile) {
        if (communicationProfile == null || communicationProfile.getEndpointUrl() == null || communicationProfile.getEndpointUrl().isBlank()) {
            throw new IllegalStateException("EMAIL communication profile endpointUrl recipient list is required");
        }
        return Arrays.stream(communicationProfile.getEndpointUrl().split(","))
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .toArray(String[]::new);
    }
}
