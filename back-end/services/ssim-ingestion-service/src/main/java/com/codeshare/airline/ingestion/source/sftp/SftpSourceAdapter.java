package com.codeshare.airline.ingestion.source.sftp;

import com.codeshare.airline.ingestion.config.SourceProperties;
import com.codeshare.airline.ingestion.model.SsimRawFile;
import com.codeshare.airline.ingestion.model.SsimSourceMetadata;
import com.codeshare.airline.ingestion.source.SourceType;
import com.codeshare.airline.ingestion.source.SsimSourceAdapter;
import com.codeshare.airline.ingestion.source.common.ChecksumUtils;
import com.codeshare.airline.ingestion.source.common.SourceChecksumRegistry;
import com.codeshare.airline.ingestion.source.sftp.client.SftpClientProvider;
import com.codeshare.airline.ingestion.source.sftp.client.SftpUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.sftp.client.SftpClient;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class SftpSourceAdapter implements SsimSourceAdapter {

    private final SourceProperties props;
    private final SftpClientProvider sftpProvider;
    private final SourceChecksumRegistry checksumRegistry;

    @Override
    public SourceType sourceType() {
        return SourceType.SFTP;
    }

    @Override
    public boolean isEnabled() {
        return props.getSftp().isEnabled();
    }

    @Override
    public List<SsimRawFile> poll() {

        SourceProperties.Sftp cfg = props.getSftp();

        return sftpProvider.execute(sftp -> {

            List<SsimRawFile> files = new ArrayList<>();

            for (SftpClient.DirEntry entry : sftp.readDir(cfg.getInboundPath())) {
                String name = entry.getFilename();

                // Skip special entries
                if (".".equals(name) || "..".equals(name)) continue;

                // Only SSIM files
                if (!name.endsWith(".ssim")) continue;

                // Skip empty / partial files
                if (entry.getAttributes().getSize() <= 0) continue;

                try {
                    byte[] content = SftpUtils.readFile(
                            sftp, cfg.getInboundPath() + "/" + name);

                    String checksum = ChecksumUtils.sha256(content);
                    if (checksumRegistry.alreadySeen(checksum)) {
                        continue;
                    }

                    checksumRegistry.markSeen(checksum);

                    files.add(SsimRawFile.builder()
                            .fileId(UUID.randomUUID().toString())
                            .originalFileName(name)
                            .content(content)
                            .sourceType(SourceType.SFTP)
                            .receivedAt(Instant.now())
                            .metadata(SsimSourceMetadata.builder().build())
                            .build());

                    log.debug("SSIM SFTP file accepted [name={}, size={}]",
                            name, entry.getAttributes().getSize());

                } catch (Exception ex) {
                    log.error("Failed to read SFTP file {}", name, ex);
                }
            }

            log.info("SFTP poll completed [files={}]", files.size());
            return files;
        });
    }

    @Override
    public void onSuccess(SsimRawFile file) {
        sftpProvider.execute(sftp -> {
            SftpUtils.move(
                    sftp,
                    props.getSftp().getInboundPath(),
                    props.getSftp().getArchivePath(),
                    file.getOriginalFileName());
            return null;
        });
    }

    @Override
    public void onFailure(SsimRawFile file, Exception e) {
        sftpProvider.execute(sftp -> {
            SftpUtils.move(
                    sftp,
                    props.getSftp().getInboundPath(),
                    props.getSftp().getErrorPath(),
                    file.getOriginalFileName());
            return null;
        });
    }
}
