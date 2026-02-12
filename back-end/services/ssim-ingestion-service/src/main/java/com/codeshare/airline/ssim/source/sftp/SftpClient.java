package com.codeshare.airline.ssim.source.sftp;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class SftpClient {

    private final SftpSourceConfig config;

    /**
     * List remote SSIM files available on the SFTP server.
     */
    public Stream<String> listRemoteFiles() {
        // connect via JSch / SSHJ and list files
        return Stream.empty();
    }

    /**
     * Returns a NEW streaming InputStream for the given remote file.
     * Caller is responsible for closing the stream.
     *
     * IMPORTANT:
     * - Must be streaming
     * - Must NOT buffer entire file
     */
    public InputStream openStream(String remotePath) {
        throw new UnsupportedOperationException(
                "SFTP streaming not implemented yet"
        );
    }

    public String host() {
        return config.getHost();
    }
}
