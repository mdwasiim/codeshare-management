package com.codeshare.airline.ssim.inbound.source;

import com.codeshare.airline.ssim.inbound.domain.enums.SsimSourceType;
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
public class SsimSourceFile {

    /**
     * Unique identifier for this ingestion event
     */
    private final String fileId;

    /**
     * Optional batch / load correlation ID
     */
    private final UUID loadId;

    /**
     * Original file name
     */
    private final String fileName;

    /**
     * Source type (LOCAL, SFTP, EMAIL, REST, KAFKA)
     */
    private final SsimSourceType sourceType;

    /**
     * Source system identifier
     * (path, host, mailbox, topic, etc.)
     */
    private final String sourceSystem;

    /**
     * When the file was received / discovered
     */
    private final Instant receivedAt;

    /**
     * Optional external reference
     */
    private final String externalReference;

    /**
     * Lazy stream supplier (CRITICAL)
     * File content is NOT loaded until this is called.
     */
    private final Supplier<InputStream> streamSupplier;


    private final Long fileSizeBytes;


    /**
     * Opens a NEW InputStream for reading file content.
     *
     * <p>Each invocation must return a fresh stream.
     * Callers are responsible for closing the stream unless using withStream().
     */
    public InputStream openStream() {
        if (streamSupplier == null) {
            throw new IllegalStateException(
                    "streamSupplier is null for fileId=" + fileId
            );
        }
        return streamSupplier.get();
    }

    // ✅ FOR return values (profile detection)
    public <T> T withStream(Function<InputStream, T> fn) {
        try (InputStream is = openStream()) {
            return fn.apply(is);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Failed to process stream for fileId=" + fileId, e
            );
        }
    }

    // ✅ FOR void operations (loading)
    public void consumeStream(Consumer<InputStream> consumer) {
        try (InputStream is = openStream()) {
            consumer.accept(is);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Failed to process stream for fileId=" + fileId, e
            );
        }
    }
}
