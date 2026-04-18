package com.codeshare.airline.inbound.source.inbound;

import com.codeshare.airline.enums.MessageType;
import com.codeshare.airline.inbound.domain.enums.ProcessingStatus;
import com.codeshare.airline.inbound.domain.enums.ScheduleProfile;
import com.codeshare.airline.inbound.domain.enums.SourceType;
import com.codeshare.airline.inbound.domain.enums.TimeMode;
import com.codeshare.airline.ingestion.domain.enums.*;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

@Getter
@Builder(toBuilder = true)
@ToString(exclude = "streamSupplier")
public class ScheduleSourceFile {

    /* ===================== IDENTITY ===================== */

    private final UUID fileId;     // unique identifier for ingestion
    private final UUID loadId;     // optional batch correlation

    /* ===================== OWNERSHIP ===================== */

    private final String airlineCode;

    /* ===================== ORIGIN ===================== */

    private final String fileName;
    private final SourceType sourceType;
    private final MessageType messageType;
    private final ScheduleProfile scheduleProfile;
    private final ProcessingStatus processingStatus;
    private TimeMode timeMode;
    /* ===================== METADATA ===================== */

    private final Long fileSizeBytes;
    private final String checksum;

    /* ===================== STREAM ACCESS ===================== */

    private final Supplier<InputStream> streamSupplier;

    public InputStream openStream() {
        if (streamSupplier == null) {
            throw new IllegalStateException("streamSupplier is null for fileId=" + fileId);
        }
        return streamSupplier.get();
    }

    public <T> T withStream(Function<InputStream, T> fn) {
        try (InputStream is = openStream()) {
            return fn.apply(is);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to process stream for fileId=" + fileId, e);
        }
    }

    public <T> T consumeStream(Function<InputStream, T> function) {
        try (InputStream is = openStream()) {
            return function.apply(is);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Failed to process stream for fileId=" + fileId, e);
        }
    }
}