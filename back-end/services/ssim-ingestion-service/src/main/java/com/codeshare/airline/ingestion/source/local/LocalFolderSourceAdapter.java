package com.codeshare.airline.ingestion.source.local;

import com.codeshare.airline.ingestion.config.SourceProperties;
import com.codeshare.airline.ingestion.model.SsimRawFile;
import com.codeshare.airline.ingestion.source.SourceType;
import com.codeshare.airline.ingestion.source.SsimSourceAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class LocalFolderSourceAdapter implements SsimSourceAdapter {

    private final SourceProperties properties;

    @Override
    public SourceType sourceType() {
        return SourceType.LOCAL;
    }

    @Override
    public boolean isEnabled() {
        return properties.getLocal().isEnabled();
    }

    @Override
    public List<SsimRawFile> poll() {

        Path dir = Paths.get(properties.getLocal().getPath());
        if (!Files.exists(dir)) return List.of();

        try (Stream<Path> files = Files.list(dir)) {
            return files
                    .filter(Files::isRegularFile)
                    .map(this::toRawFile)
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onSuccess(SsimRawFile file) {

    }

    @Override
    public void onFailure(SsimRawFile file, Exception e) {

    }

    private SsimRawFile toRawFile(Path path) {
        try {
            return SsimRawFile.builder()
                    .fileId(UUID.randomUUID().toString())
                    .originalFileName(path.getFileName().toString())
                    .content(Files.readAllBytes(path))
                    .sourceType(SourceType.LOCAL)
                    .receivedAt(Instant.now())
                    .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
