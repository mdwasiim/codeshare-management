package com.codeshare.airline.schedule.source;

import com.codeshare.airline.schedule.domain.common.ScheduleMessageType;
import com.codeshare.airline.schedule.domain.common.ScheduleSourceType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Getter
@Builder
@ToString(exclude = "streamSupplier")
public class ScheduleSourceFile {

    private final String fileId;

    private final UUID loadId;

    private final String airlineCode;   // ✅ Added

    private final String fileName;

    private final ScheduleSourceType sourceType;

    private final ScheduleMessageType scheduleMessageType;

    private final String sourceSystem;

    private final Instant receivedAt;

    private final String externalReference;

    private final Supplier<InputStream> streamSupplier;

    private final Long fileSizeBytes;

    private final String checksum;      // optional but recommended

    public InputStream openStream() {
        if (streamSupplier == null) {
            throw new IllegalStateException(
                    "streamSupplier is null for fileId=" + fileId);
        }
        return streamSupplier.get();
    }

    public <T> T withStream(Function<InputStream, T> fn) {
        try (InputStream is = openStream()) {
            return fn.apply(is);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Failed to process stream for fileId=" + fileId, e);
        }
    }

    public void consumeStream(Consumer<InputStream> consumer) {
        try (InputStream is = openStream()) {
            consumer.accept(is);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Failed to process stream for fileId=" + fileId, e);
        }
    }
}
