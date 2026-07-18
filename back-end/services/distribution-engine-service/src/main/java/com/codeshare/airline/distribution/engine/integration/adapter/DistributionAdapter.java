package com.codeshare.airline.distribution.engine.integration.adapter;

import com.codeshare.airline.platform.core.dto.master.codesharepartner.CodesharePartnerDistributionProfileDTO;
import com.codeshare.airline.platform.core.dto.master.codesharepartner.CodesharePartnerCommunicationProfileDTO;
import com.codeshare.airline.platform.core.dto.schedule.workflow.OutboundScheduleMessageDTO;
import com.codeshare.airline.platform.core.enums.master.codesharepartner.CommunicationProtocol;
import com.codeshare.airline.platform.core.events.schedule.DistributionRequestedEvent;

public interface DistributionAdapter {

    CommunicationProtocol supports();

    void dispatch(
            DistributionRequestedEvent event,
            CodesharePartnerDistributionProfileDTO profile,
            CodesharePartnerCommunicationProfileDTO communicationProfile,
            OutboundScheduleMessageDTO outboundMessage
    );
}
