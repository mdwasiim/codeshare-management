package com.codeshare.airline.schedule.ingestion.source.camel.channel;

import com.codeshare.airline.platform.core.enums.schedule.SourceType;
import com.codeshare.airline.schedule.ingestion.dto.source.AirlineIngestionChannelDTO;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SftpChannelRouteBuilderTest {

    private final SftpChannelRouteBuilder builder = new SftpChannelRouteBuilder(null);

    @Test
    void normalizeRemoteDirectoryKeepsRelativePathRelativeToSftpLoginDirectory() {
        assertThat(builder.normalizeRemoteDirectory("QR/SSIM")).isEqualTo("/QR/SSIM");
    }

    @Test
    void normalizeRemoteDirectoryRemovesExplicitHomePrefixForCamelSftp() {
        assertThat(builder.normalizeRemoteDirectory("~/QR/SSIM")).isEqualTo("/QR/SSIM");
    }

    @Test
    void normalizeRemoteDirectoryKeepsAbsolutePath() {
        assertThat(builder.normalizeRemoteDirectory("/home/sftpuser/QR/SSIM")).isEqualTo("/home/sftpuser/QR/SSIM");
    }

    @Test
    void normalizeRemoteDirectoryNormalizesWindowsSeparators() {
        assertThat(builder.normalizeRemoteDirectory("QR\\SSIM")).isEqualTo("/QR/SSIM");
    }

    @Test
    void buildUriUsesBackslashFreeDefaultIncludePattern() {
        AirlineIngestionChannelDTO channel = new AirlineIngestionChannelDTO();
        channel.setSourceType(SourceType.SFTP);
        channel.setHost("127.0.0.1");
        channel.setPort(22);
        channel.setUsername("sftpuser");
        channel.setRemoteDirectory("QR/SSIM");

        assertThat(builder.buildUri(channel)).contains("include=(?i).*[.](txt|ssm|asm|ssim)");
    }
}
