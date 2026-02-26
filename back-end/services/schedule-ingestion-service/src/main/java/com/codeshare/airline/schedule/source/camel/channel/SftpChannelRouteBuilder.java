package com.codeshare.airline.schedule.source.camel.channel;

import com.codeshare.airline.schedule.domain.common.ScheduleSourceType;
import com.codeshare.airline.schedule.source.persistence.entity.AirlineIngestionChannel;
import com.codeshare.airline.schedule.source.security.CredentialResolver;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import static org.apache.camel.builder.Builder.constant;

@Component
@RequiredArgsConstructor
public class SftpChannelRouteBuilder implements ChannelRouteBuilder {

    private final CredentialResolver credentialResolver;

    @Override
    public ScheduleSourceType supports() {
        return ScheduleSourceType.SFTP;
    }

    @Override
    public void build(RouteBuilder rb,
                      AirlineIngestionChannel channel) {

        validate(channel);

        String password = resolvePassword(channel);

        String uri = buildUri(channel, password);

        String routeId = buildRouteId(channel);

        rb.from(uri)
                .routeId(routeId)
                .setHeader("AIRLINE_CODE",
                        constant(channel.getProfile().getAirlineCode()))
                .setHeader("SOURCE_TYPE",
                        constant(channel.getSourceType().name()))
                .setHeader("MESSAGE_TYPE",
                        constant(channel.getMessageType().name()))
                .to("direct:schedule-processing");
    }

    private String resolvePassword(AirlineIngestionChannel channel) {

        if (channel.getPasswordEncrypted() == null ||
                channel.getPasswordEncrypted().isBlank()) {
            return null;
        }

        return credentialResolver.decrypt(
                channel.getPasswordEncrypted());
    }

    private String buildUri(AirlineIngestionChannel channel,
                            String password) {

        String base = String.format(
                "sftp://%s:%d/%s?username=%s",
                channel.getHost(),
                channel.getPort(),
                channel.getRemoteDirectory(),
                channel.getUsername()
        );

        if (password != null) {
            // RAW prevents Camel from logging the password
            base += "&password=RAW(" + password + ")";
        }

        base += "&disconnect=true";

        return base;
    }

    private void validate(AirlineIngestionChannel channel) {

        if (channel.getHost() == null ||
                channel.getPort() == null ||
                channel.getUsername() == null ||
                channel.getRemoteDirectory() == null) {
            throw new IllegalStateException(
                    "Invalid SFTP configuration for channel "
                            + channel.getId());
        }
    }

    private String buildRouteId(AirlineIngestionChannel channel) {

        return channel.getProfile().getAirlineCode()
                + "-"
                + channel.getSourceType()
                + "-"
                + channel.getMessageType();
    }
}