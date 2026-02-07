package com.codeshare.airline.ingestion.source.sftp.client;

import com.codeshare.airline.ingestion.config.SourceProperties;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.util.io.resource.PathResource;
import org.apache.sshd.common.util.security.SecurityUtils;
import org.apache.sshd.sftp.client.SftpClient;
import org.apache.sshd.sftp.client.SftpClientFactory;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.security.KeyPair;
import java.time.Duration;

@Component
@RequiredArgsConstructor
@Slf4j
public class SftpClientProvider {

    private final SourceProperties props;
    private SshClient sshClient;

    // ================= Lifecycle =================

    @PostConstruct
    void startClient() {
        sshClient = SshClient.setUpDefaultClient();
        sshClient.start();
        log.info("SFTP client started");
    }

    @PreDestroy
    void stopClient() {
        if (sshClient != null) {
            sshClient.stop();
            log.info("SFTP client stopped");
        }
    }

    // ================= Execution =================

    public <T> T execute(SftpSessionCallback<T> callback) {

        SourceProperties.Sftp cfg = props.getSftp();

        try (ClientSession session =
                     sshClient.connect(cfg.getUser(), cfg.getHost(), cfg.getPort())
                             .verify(Duration.ofSeconds(10))
                             .getSession()) {

            authenticate(session, cfg);

            session.auth().verify(Duration.ofSeconds(10));

            SftpClientFactory factory = SftpClientFactory.instance();

            try (SftpClient sftp = factory.createSftpClient(session)) {
                return callback.doInSession(sftp);
            }


        } catch (Exception e) {
            log.error("SFTP execution failed [host={}, user={}]",
                    cfg.getHost(), cfg.getUser(), e);
            throw new RuntimeException("SFTP execution failed", e);
        }
    }

    // ================= Auth =================

    private void authenticate(ClientSession session, SourceProperties.Sftp cfg) {

        try {
            if (cfg.getPrivateKeyPath() != null && !cfg.getPrivateKeyPath().isBlank()) {

                Iterable<KeyPair> keys =
                        SecurityUtils.loadKeyPairIdentities(
                                session,
                                new PathResource(Paths.get(cfg.getPrivateKeyPath())),
                                null,           // InputStream
                                null            // FilePasswordProvider (null = unencrypted key)
                        );

                for (KeyPair kp : keys) {
                    session.addPublicKeyIdentity(kp);
                }

            } else if (cfg.getPassword() != null && !cfg.getPassword().isBlank()) {

                session.addPasswordIdentity(cfg.getPassword());

            } else {
                throw new IllegalStateException(
                        "No SFTP authentication configured (privateKeyPath or password required)"
                );
            }

        } catch (Exception e) {
            throw new RuntimeException("SFTP authentication setup failed", e);
        }
    }


}
