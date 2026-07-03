package com.codeshare.airline.schedule.ingestion.source.camel.channel;

import com.codeshare.airline.schedule.ingestion.domain.enums.SourceType;
import com.codeshare.airline.schedule.ingestion.persistence.entities.source.ScheduleIngestionChannelEntity;
import com.codeshare.airline.schedule.ingestion.source.security.ScheduleCredentialResolver;
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

        StringBuilder uri = new StringBuilder(
                String.format("sftp://%s:%d%s?username=%s",
                        c.getHost(),
                        c.getPort(),
                        normalizeRemoteDirectory(c.getRemoteDirectory()),
                        c.getUsername())
        );

        String password = resolvePassword(c);
        if (password != null && !password.isBlank()) {
            uri.append("&password=RAW(").append(password).append(")");
        }

        return uri
                .append("&disconnect=").append(val(c.getDisconnect(), false))
                .append("&readLock=").append(val(c.getFileReadLock(), "changed"))
                .append("&readLockMinAge=").append(val(c.getFileReadLockMinAge(), "1s"))
                .append("&readLockCheckInterval=").append(val(c.getFileReadLockCheckInterval(), 1000))
                .append("&readLockTimeout=").append(val(c.getFileReadLockTimeout(), 60000))
                .append("&delay=").append(val(c.getFilePollDelayMs(), 60000))
                .append("&initialDelay=").append(val(c.getFileInitialDelayMs(), 2000))
                .append("&maxMessagesPerPoll=").append(val(c.getMaxMessagesPerPoll(), 5))
                .append("&reconnectDelay=").append(val(c.getReconnectDelayMs(), 5000))
                .append("&maximumReconnectAttempts=").append(val(c.getMaximumReconnectAttempts(), 3))
                .append("&preMove=").append(val(c.getFilePreMove(), ".inprogress/${file:name}"))
                .append("&move=").append(val(c.getFileMove(), ".processed/${file:name}"))
                .append("&moveFailed=").append(val(c.getFileMoveFailed(), ".error/${file:name}"))
                .append("&include=").append(val(c.getFileIncludePattern(), "(?i).*\\.(txt|ssm|asm|ssim)"))
                .append("&idempotent=").append(val(c.getFileIdempotent(), true))
                .append("&idempotentKey=").append(val(c.getFileIdempotentKey(),
                        "${file:absolute.path}-${file:modified}"))
                .append("&recursive=").append(val(c.getRecursive(), false))
                .append("&bridgeErrorHandler=").append(val(c.getBridgeErrorHandler(), true))
                .toString();
    }

    @Override
    protected void validate(ScheduleIngestionChannelEntity c) {
        if (c.getHost() == null || c.getHost().isBlank()) {
            throw new IllegalStateException("SFTP host is required");
        }
        if (c.getPort() == null) {
            throw new IllegalStateException("SFTP port is required");
        }
        if (c.getUsername() == null || c.getUsername().isBlank()) {
            throw new IllegalStateException("SFTP username is required");
        }
        if (c.getRemoteDirectory() == null || c.getRemoteDirectory().isBlank()) {
            throw new IllegalStateException("SFTP remote directory is required");
        }
    }

    private String resolvePassword(ScheduleIngestionChannelEntity c) {
        return c.getPasswordEncrypted() != null && !c.getPasswordEncrypted().isBlank()
                ? resolver.decrypt(c.getPasswordEncrypted())
                : null;
    }

    private String normalizeRemoteDirectory(String directory) {
        String normalized = directory.replace("\\", "/");
        return normalized.startsWith("/") ? normalized : "/" + normalized;
    }
}
