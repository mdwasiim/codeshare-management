package com.codeshare.airline.ssim.source.local;

import com.codeshare.airline.ssim.source.SsimSource;
import com.codeshare.airline.ssim.source.SsimSourceFile;
import com.codeshare.airline.ssim.source.SsimSourceType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Component
public class LocalSsimSource implements SsimSource {

    private final Path directory;

    public LocalSsimSource( @Value("${ssim.source.local.path}") Path directory) {
        this.directory = directory;
    }

    @Override
    public SsimSourceType getType() {
        return SsimSourceType.LOCAL;
    }

    @Override
    public List<SsimSourceFile> fetch() {

        try (Stream<Path> paths = Files.list(directory)) {

            return paths
                    .filter(Files::isRegularFile)
                    .map(this::toSourceFile)
                    .toList();

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Failed to list SSIM directory: " + directory, e
            );
        }
    }

    private SsimSourceFile toSourceFile(Path path) {
        return SsimSourceFile.builder()
                .fileId(UUID.randomUUID().toString())
                .fileName(path.getFileName().toString())
                .sourceType(getType())
                .sourceSystem(directory.toString())
                .receivedAt(Instant.now())
                .streamSupplier(() -> {
                    try {
                        return Files.newInputStream(path);
                    } catch (IOException e) {
                        throw new IllegalStateException(
                                "Failed to open SSIM file: " + path, e
                        );
                    }
                })
                .build();
    }

}
