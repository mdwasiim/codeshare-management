package com.codeshare.airline.distribution.engine.integration.adapter;

import com.codeshare.airline.platform.core.dto.master.codesharepartner.CodesharePartnerCommunicationProfileDTO;
import com.codeshare.airline.platform.core.dto.master.codesharepartner.CodesharePartnerDistributionProfileDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.OutboundScheduleMessageDTO;
import com.codeshare.airline.platform.core.enums.master.codesharepartner.CommunicationProtocol;
import com.codeshare.airline.platform.core.events.schedule.DistributionRequestedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SftpDistributionAdapter implements DistributionAdapter {

    @Override
    public CommunicationProtocol supports() {
        return CommunicationProtocol.SFTP;
    }

    @Override
    public void dispatch(
            DistributionRequestedEvent event,
            CodesharePartnerDistributionProfileDTO profile,
            CodesharePartnerCommunicationProfileDTO communicationProfile,
            OutboundScheduleMessageDTO outboundMessage
    ) {
        log.info(
                "Distribution adapter SFTP dispatched endpoint={} changeSetId={} outboundMessageId={} partner={} profile={} payloadLength={}",
                communicationProfile != null ? communicationProfile.getEndpointUrl() : null,
                event.getChangeSetId(),
                outboundMessage.getOutboundMessageId(),
                event.getPartnerCode(),
                profile.getProfileCode(),
                outboundMessage.getPayload() != null ? outboundMessage.getPayload().length() : 0
        );
    }
}
