package com.codeshare.airline.schedule.ingestion.source.camel.channel;

import com.codeshare.airline.core.enums.schedule.SourceType;
import com.codeshare.airline.schedule.ingestion.dto.source.AirlineIngestionChannelDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LocalFileChannelRouteBuilder extends AbstractChannelRouteBuilder {

    @Override
    public SourceType supports() {
        return SourceType.LOCAL;
    }

    @Override
    protected String buildUri(AirlineIngestionChannelDTO c) {

        String dir = normalizePath(c.getRemoteDirectory());

        return new StringBuilder("file:" + dir)
                .append("?noop=false")

                // 🔥 FILE FILTER
                .append("&include=").append(val(c.getFileIncludePattern(), "(?i).*\\.(txt|ssm|asm|ssim)"))

                // 🔥 SAFE PROCESSING (IMPORTANT)
                .append("&preMove=").append(val(c.getFilePreMove(), ".inprogress/${file:name}"))
                .append("&move=").append(val(c.getFileMove(), ".processed/${date:now:yyyyMMdd}/${file:name}"))
                .append("&moveFailed=").append(val(c.getFileMoveFailed(), ".error/${date:now:yyyyMMdd}/${file:name}"))

                // 🔥 READ LOCK (MANDATORY)
                .append("&readLock=").append(val(c.getFileReadLock(), "changed"))
                .append("&readLockMinAge=").append(val(c.getFileReadLockMinAge(), "2s"))
                .append("&readLockCheckInterval=").append(val(c.getFileReadLockCheckInterval(), 1000))
                .append("&readLockTimeout=").append(val(c.getFileReadLockTimeout(), 60000))

                // 🔥 POLLING
                .append("&delay=").append(val(c.getFilePollDelayMs(), 60000))
                .append("&initialDelay=").append(val(c.getFileInitialDelayMs(), 2000))

                // 🔥 ENCODING
                .append("&charset=").append(val(c.getFileCharset(), "UTF-8"))

                // 🔥 DUPLICATE PROTECTION
                .append("&idempotent=").append(val(c.getFileIdempotent(), true))
                .append("&idempotentKey=").append(val(c.getFileIdempotentKey(),
                        "${file:absolute.path}-${file:modified}"))

                // 🔥 PERFORMANCE
                .append("&maxMessagesPerPoll=").append(val(c.getMaxMessagesPerPoll(), 10))
                .append("&recursive=").append(val(c.getRecursive(), false))

                // 🔥 ERROR HANDLING
                .append("&bridgeErrorHandler=").append(val(c.getBridgeErrorHandler(), true))

                .toString();
    }

    @Override
    protected void validate(AirlineIngestionChannelDTO c) {
        if (c.getRemoteDirectory() == null || c.getRemoteDirectory().isBlank()) {
            throw new IllegalStateException("Local directory is required");
        }
    }

    private String normalizePath(String path) {
        return path.replace("\\", "/");
    }
}
