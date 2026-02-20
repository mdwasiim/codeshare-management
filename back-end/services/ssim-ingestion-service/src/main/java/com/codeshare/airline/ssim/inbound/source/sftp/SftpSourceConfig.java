package com.codeshare.airline.ssim.inbound.source.sftp;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ssim.source.sftp")
@Getter
@Setter
public class SftpSourceConfig {

    private String host;
    private int port = 22;

    private String username;
    private String password;

    private String remoteDirectory;
    private String filePattern = "*.ssim";

    private boolean deleteAfterDownload = false;
}
