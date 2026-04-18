/*
package com.codeshare.airline.ingestion.source.camel.channel;

import com.codeshare.airline.ingestion.domain.enums.SourceType;
import com.codeshare.airline.ingestion.persistence.entities.source.ScheduleIngestionChannelEntity;
import com.codeshare.airline.ingestion.source.security.ScheduleCredentialResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SftpChannelRouteBuilder extends AbstractChannelRouteBuilder {

    private final ScheduleCredentialResolver resolver;

    @Override
    public SourceType supports() {
        return SourceType.SFTP;
    }

    @Override
    protected String buildUri(ScheduleIngestionChannelEntity c) {

        String password = resolvePassword(c);

        StringBuilder uri = new StringBuilder(
                String.format("sftp://%s:%d/%s?username=%s",
                        c.getHost(), c.getPort(),
                        c.getRemoteDirectory(), c.getUsername())
        );

        if (password != null) {
            uri.append("&password=RAW(").append(password).append(")");
        }

        return uri
                .append("&disconnect=").append(val(c.getDisconnect(), false))
                .append("&binary=").append(val(c.getBinary(), true))
                .append("&passiveMode=").append(val(c.getPassiveMode(), true))

                .append("&readLock=").append(val(c.getFileReadLock(), "changed"))
                .append("&readLockMinAge=").append(val(c.getFileReadLockMinAge(), "1s"))
                .append("&readLockTimeout=").append(val(c.getFileReadLockTimeout(), 60000))

                .append("&delay=").append(val(c.getFilePollDelayMs(), 60000))

                .append("&maxMessagesPerPoll=").append(val(c.getMaxMessagesPerPoll(), 5))

                .append("&reconnectDelay=").append(val(c.getReconnectDelayMs(), 5000))
                .append("&maximumReconnectAttempts=").append(val(c.getMaximumReconnectAttempts(), 3))

                .append("&preMove=").append(val(c.getFilePreMove(), ".inprogress/${file:name}"))
                .append("&move=").append(val(c.getFileMove(), ".processed/${file:name}"))
                .append("&moveFailed=").append(val(c.getFileMoveFailed(), ".error/${file:name}"))

                .append("&idempotent=").append(val(c.getFileIdempotent(), true))
                .append("&idempotentKey=").append(val(c.getFileIdempotentKey(),
                        "${file:absolute.path}-${file:modified}"))

                .toString();
    }

    @Override
    protected void validate(ScheduleIngestionChannelEntity c) {
        if (c.getHost() == null || c.getPort() == null || c.getRemoteDirectory() == null) {
            throw new IllegalStateException("Invalid SFTP configuration");
        }
    }

    private String resolvePassword(ScheduleIngestionChannelEntity c) {
        return c.getPasswordEncrypted() != null
                ? resolver.decrypt(c.getPasswordEncrypted())
                : null;
    }

    private <T> T defaultVal(T val, T def) {
        return val != null ? val : def;
    }
}
*/
