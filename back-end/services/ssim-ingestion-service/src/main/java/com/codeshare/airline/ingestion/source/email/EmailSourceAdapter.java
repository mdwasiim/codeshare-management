package com.codeshare.airline.ingestion.source.email;

import com.codeshare.airline.ingestion.config.SourceProperties;
import com.codeshare.airline.ingestion.model.SsimRawFile;
import com.codeshare.airline.ingestion.model.SsimSourceMetadata;
import com.codeshare.airline.ingestion.source.SourceType;
import com.codeshare.airline.ingestion.source.SsimSourceAdapter;
import com.codeshare.airline.ingestion.source.common.ChecksumUtils;
import com.codeshare.airline.ingestion.source.common.SourceChecksumRegistry;
import jakarta.mail.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailSourceAdapter implements SsimSourceAdapter {

    private final SourceProperties props;
    private final EmailClientProvider emailClient;
    private final SourceChecksumRegistry checksumRegistry;

    @Override
    public SourceType sourceType() {
        return SourceType.EMAIL;
    }

    @Override
    public boolean isEnabled() {
        return props.getEmail().isEnabled();
    }

    @Override
    public List<SsimRawFile> poll() {

        List<SsimRawFile> files = new ArrayList<>();

        try (Store store = emailClient.connect()) {

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

            for (Message msg : inbox.getMessages()) {

                if (!(msg.getContent() instanceof Multipart mp)) continue;

                for (int i = 0; i < mp.getCount(); i++) {
                    BodyPart part = mp.getBodyPart(i);

                    if (!Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                        continue;
                    }

                    byte[] content = part.getInputStream().readAllBytes();
                    String checksum = ChecksumUtils.sha256(content);

                    if (checksumRegistry.alreadySeen(checksum)) continue;
                    checksumRegistry.markSeen(checksum);

                    files.add(SsimRawFile.builder()
                            .fileId(UUID.randomUUID().toString())
                            .originalFileName(part.getFileName())
                            .content(content)
                            .sourceType(SourceType.EMAIL)
                            .receivedAt(Instant.now())
                            .metadata(SsimSourceMetadata.builder().build())
                            .build());
                }

                msg.setFlag(Flags.Flag.SEEN, true);
            }

        } catch (Exception e) {
            log.error("Email polling failed", e);
        }

        return files;
    }

    @Override
    public void onSuccess(SsimRawFile file) {
        // already marked as SEEN
    }

    @Override
    public void onFailure(SsimRawFile file, Exception e) {
        // optionally move to ERROR folder
    }
}

