package com.codeshare.airline.ssim.inbound.source.cmel;

import com.codeshare.airline.ssim.inbound.domain.enums.SsimSourceType;
import com.codeshare.airline.ssim.inbound.orchestration.SsimProcessor;
import com.codeshare.airline.ssim.inbound.source.SsimSourceFile;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.UUID;

public abstract class AbstractSsimSourceRoute extends RouteBuilder {

    protected final SsimProcessor ssimProcessor;

    protected AbstractSsimSourceRoute(SsimProcessor ssimProcessor) {
        this.ssimProcessor = ssimProcessor;
    }

    /** Camel endpoint URI (file:, sftp:, cloud:) */
    protected abstract String endpointUri();

    /** LOCAL / SFTP / CLOUD */
    protected abstract SsimSourceType sourceType();

    /** Human-readable source identifier */
    protected abstract String sourceSystem(Exchange exchange);

    /** Route ID */
    protected abstract String routeId();

    @Override
    public final void configure() {

        /*from(endpointUri())
                .routeId(routeId())
                .process(exchange -> {

                    String fileName =
                            exchange.getIn().getHeader(
                                    Exchange.FILE_NAME,
                                    String.class);

                    String localPath =
                            exchange.getIn().getHeader(
                                    Exchange.FILE_LOCAL_WORK_PATH,
                                    String.class);

                    SsimSourceFile sourceFile =
                            SsimSourceFile.builder()
                                    .fileId(UUID.randomUUID().toString())
                                    .fileName(fileName)
                                    .sourceType(sourceType())
                                    .sourceSystem(sourceSystem(exchange))
                                    .receivedAt(Instant.now())
                                    .streamSupplier(() -> openStream(localPath))
                                    .build();

                    ssimProcessor.process(sourceFile);
                });*/
        from(endpointUri())
                .routeId(routeId())
                .process(exchange -> {

                    String fileName =
                            exchange.getIn().getHeader(
                                    Exchange.FILE_NAME,
                                    String.class);

                    SsimSourceFile sourceFile =
                            SsimSourceFile.builder()
                                    .fileId(UUID.randomUUID().toString())
                                    .fileName(fileName)
                                    .sourceType(sourceType())
                                    .sourceSystem(sourceSystem(exchange))
                                    .receivedAt(Instant.now())
                                    .streamSupplier(() ->
                                            exchange.getIn().getBody(InputStream.class))
                                    .build();

                    ssimProcessor.process(sourceFile);
                });

    }

    protected java.io.InputStream openStream(String localPath) {
        try {
            return Files.newInputStream(Path.of(localPath));
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Failed to open SSIM file: " + localPath, e);
        }
    }
}
