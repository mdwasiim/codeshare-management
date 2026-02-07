package com.codeshare.airline.ingestion.source.sftp.client;

import org.apache.sshd.sftp.client.SftpClient;

@FunctionalInterface
public interface SftpSessionCallback<T> {
    T doInSession(SftpClient sftp) throws Exception;
}
