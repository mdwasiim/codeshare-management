package com.codeshare.airline.ingestion.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "ssim.sources")
@Getter
@Setter
@Validated
public class SourceProperties {

    private Local local = new Local();
    private Sftp sftp = new Sftp();
    private Email email = new Email();

    // ================= LOCAL =================
    @Getter @Setter
    public static class Local {
        private boolean enabled = false;

        @NotBlank
        private String path;
    }

    // ================= SFTP =================
    @Getter @Setter
    public static class Sftp {
        private boolean enabled = false;

        @NotBlank
        private String host;

        private int port = 22;

        @NotBlank
        private String user;

        private String password;          // optional
        private String privateKeyPath;    // preferred in prod

        @NotBlank
        private String inboundPath;

        @NotBlank
        private String archivePath;

        @NotBlank
        private String errorPath;
    }

    // ================= EMAIL =================
    @Getter @Setter
    public static class Email {
        private boolean enabled = false;

        @NotBlank
        private String host;

        private int port = 993;
        private String protocol = "imaps";

        @NotBlank
        private String user;

        @NotBlank
        private String password;

        private String inboxFolder = "INBOX";
        private String processedFolder = "PROCESSED";
        private String errorFolder = "ERROR";

        private String attachmentPattern = ".*\\.ssim";
    }
}
