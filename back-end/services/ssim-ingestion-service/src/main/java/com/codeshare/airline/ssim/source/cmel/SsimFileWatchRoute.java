package com.codeshare.airline.ssim.source.cmel;

import com.codeshare.airline.ssim.processor.SsimProcessor;
import com.codeshare.airline.ssim.source.SsimSourceFile;
import com.codeshare.airline.ssim.source.SsimSourceType;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SsimFileWatchRoute extends RouteBuilder {

    private final SsimProcessor ssimProcessor;

    @Override
    public void configure() {

        from("file:/opt/ssim/inbound"
                + "?readLock=changed"
                + "&readLockCheckInterval=2000"
                + "&readLockTimeout=30000"
                + "&move=.processed"
                + "&moveFailed=.error"
                + "&idempotent=true"
                + "&idempotentRepository=#fileIdempotentRepo")
                .routeId("ssim-file-watch")
                .process(exchange -> {

                    File file =
                            exchange.getIn().getBody(File.class);

                    String localPath =
                            exchange.getIn().getHeader(
                                    org.apache.camel.Exchange.FILE_LOCAL_WORK_PATH,
                                    String.class);

                    SsimSourceFile sourceFile =
                            SsimSourceFile.builder()
                                    .fileId(UUID.randomUUID().toString())
                                    .fileName(file.getName())
                                    .sourceType(SsimSourceType.LOCAL)
                                    .sourceSystem(file.getParent())
                                    .receivedAt(Instant.now())
                                    .streamSupplier(() ->
                                    {
                                        try {
                                            return Files.newInputStream(Path.of(localPath));
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    })
                                    .build();

                    ssimProcessor.process(sourceFile);
                });


    }
}
