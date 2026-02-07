package com.codeshare.airline.ingestion.source.sftp.client;

import org.apache.sshd.sftp.client.SftpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class SftpUtils {

    private SftpUtils() {}

    public static byte[] readFile(SftpClient sftp, String path) throws IOException {

        try (InputStream is = sftp.read(path);
             ByteArrayOutputStream os = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[8192];
            int read;
            while ((read = is.read(buffer)) != -1) {
                os.write(buffer, 0, read);
            }
            return os.toByteArray();
        }
    }


    /**
     * Moves a remote file using SFTP rename.
     * Assumes atomic rename support on the SFTP server.
     */
    public static void move(
            SftpClient sftp,
            String fromDir,
            String toDir,
            String fileName) throws IOException {

        sftp.rename(
                path(fromDir, fileName),
                path(toDir, fileName)
        );
    }

    private static String path(String dir, String file) {
        return dir.endsWith("/") ? dir + file : dir + "/" + file;
    }
}


