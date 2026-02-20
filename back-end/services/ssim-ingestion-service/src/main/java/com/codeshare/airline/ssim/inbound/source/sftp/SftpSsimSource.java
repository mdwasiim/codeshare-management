package com.codeshare.airline.ssim.inbound.source.sftp;


import com.codeshare.airline.ssim.inbound.source.SsimSource;
import com.codeshare.airline.ssim.inbound.source.SsimSourceFile;
import com.codeshare.airline.ssim.inbound.domain.enums.SsimSourceType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Profile("sftp")
public class SftpSsimSource implements SsimSource {

    private final SftpClient client;

    @Override
    public Iterable<SsimSourceFile> fetch() {

        try {
            return client.listRemoteFiles()
                    .map(this::toSourceFile)
                    .toList();
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to list SSIM files from SFTP host=" + client.host(),
                    e
            );
        }
    }

    private SsimSourceFile toSourceFile(String path) {

        return SsimSourceFile.builder()
                .fileName(path)
                .sourceType(SsimSourceType.SFTP)
                .sourceSystem(client.host())
                .receivedAt(Instant.now())
                .streamSupplier(() ->
                        client.openStream(path)
                )
                .build();
    }

    @Override
    public SsimSourceType getType() {
        return SsimSourceType.SFTP;
    }
}
