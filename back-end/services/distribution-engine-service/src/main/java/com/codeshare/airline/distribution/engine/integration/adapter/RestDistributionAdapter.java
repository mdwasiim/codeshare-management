package com.codeshare.airline.distribution.engine.integration.adapter;

import com.codeshare.airline.platform.core.dto.master.codesharepartner.CodesharePartnerCommunicationProfileDTO;
import com.codeshare.airline.platform.core.dto.master.codesharepartner.CodesharePartnerDistributionProfileDTO;
import com.codeshare.airline.platform.core.enums.master.codesharepartner.AuthenticationType;
import com.codeshare.airline.platform.core.dto.schedule.workflow.OutboundScheduleMessageDTO;
import com.codeshare.airline.platform.core.enums.master.codesharepartner.CommunicationProtocol;
import com.codeshare.airline.platform.core.events.schedule.DistributionRequestedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class RestDistributionAdapter implements DistributionAdapter {

    private final RestClient restClient;

    public RestDistributionAdapter(RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    @Override
    public CommunicationProtocol supports() {
        return CommunicationProtocol.API;
    }

    @Override
    public void dispatch(
            DistributionRequestedEvent event,
            CodesharePartnerDistributionProfileDTO profile,
            CodesharePartnerCommunicationProfileDTO communicationProfile,
            OutboundScheduleMessageDTO outboundMessage
    ) {
        String endpoint = requireEndpoint(communicationProfile);
        restClient.post()
                .uri(endpoint)
                .contentType(MediaType.TEXT_PLAIN)
                .headers(headers -> applyHeaders(headers, event, communicationProfile, outboundMessage))
                .body(outboundMessage.getPayload())
                .retrieve()
                .toBodilessEntity();
        log.info(
                "Distribution adapter REST/API dispatched endpoint={} changeSetId={} outboundMessageId={} partner={} profile={} payloadLength={}",
                endpoint,
                event.getChangeSetId(),
                outboundMessage.getOutboundMessageId(),
                event.getPartnerCode(),
                profile.getProfileCode(),
                outboundMessage.getPayload() != null ? outboundMessage.getPayload().length() : 0
        );
    }

    private void applyHeaders(
            HttpHeaders headers,
            DistributionRequestedEvent event,
            CodesharePartnerCommunicationProfileDTO communicationProfile,
            OutboundScheduleMessageDTO outboundMessage
    ) {
        headers.add("X-Correlation-Id", event.getCorrelationId() != null ? event.getCorrelationId().toString() : "");
        headers.add("X-Causation-Id", event.getCausationId() != null ? event.getCausationId().toString() : "");
        headers.add("X-Outbound-Message-Id", outboundMessage.getOutboundMessageId().toString());
        headers.add("X-Message-Type", outboundMessage.getMessageType().name());
        if (communicationProfile.getAuthenticationType() == AuthenticationType.API_KEY
                && StringUtils.hasText(communicationProfile.getCredentialAlias())) {
            headers.add("X-API-Key", communicationProfile.getCredentialAlias());
        }
        if (communicationProfile.getAuthenticationType() == AuthenticationType.BASIC
                && StringUtils.hasText(communicationProfile.getUsername())
                && StringUtils.hasText(communicationProfile.getCredentialAlias())) {
            headers.setBasicAuth(communicationProfile.getUsername(), communicationProfile.getCredentialAlias());
        }
    }

    private String requireEndpoint(CodesharePartnerCommunicationProfileDTO communicationProfile) {
        if (communicationProfile == null || communicationProfile.getEndpointUrl() == null || communicationProfile.getEndpointUrl().isBlank()) {
            throw new IllegalStateException("REST/API communication profile endpointUrl is required");
        }
        return communicationProfile.getEndpointUrl().trim();
    }
}
