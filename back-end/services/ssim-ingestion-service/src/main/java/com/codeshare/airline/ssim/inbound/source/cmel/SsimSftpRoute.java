package com.codeshare.airline.ssim.inbound.source.cmel;

import com.codeshare.airline.ssim.inbound.orchestration.SsimProcessor;
import com.codeshare.airline.ssim.inbound.domain.enums.SsimSourceType;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SsimSftpRoute extends AbstractSsimSourceRoute {

    @Value("${ssim.source.sftp.host}")
    private String host;

    @Value("${ssim.source.sftp.port}")
    private int port;

    @Value("${ssim.source.sftp.username}")
    private String username;

    @Value("${ssim.source.sftp.password}")
    private String password;

    @Value("${ssim.source.sftp.remote-directory}")
    private String remoteDirectory;

    @Value("${ssim.source.sftp.file-pattern}")
    private String filePattern;

    public SsimSftpRoute(SsimProcessor ssimProcessor) {
        super(ssimProcessor);
    }

    @Override
    protected String endpointUri() {

        return String.format(
                "sftp://%s@%s:%d%s"
                        + "?password=%s"
                        + "&binary=true"
                        + "&stepwise=false"
                        + "&disconnect=true"
                        + "&readLock=changed"
                        + "&include=%s"
                        + "&move=.processed"
                        + "&moveFailed=.error"
                        + "&delay=30000"
                        + "&maximumReconnectAttempts=5"
                        + "&throwExceptionOnConnectFailed=true"
                        + "&idempotent=true"
                        + "&idempotentKey=${file:name}-${file:size}-${file:modified}"
                        + "&idempotentRepository=#fileIdempotentRepo",
                username,
                host,
                port,
                remoteDirectory,
                password,
                filePattern.replace("*", ".*")
        );
    }


    @Override
    protected SsimSourceType sourceType() {
        return SsimSourceType.SFTP;
    }

    @Override
    protected String sourceSystem(Exchange exchange) {
        return exchange.getFromEndpoint().getEndpointUri();
    }

    @Override
    protected String routeId() {
        return "ssim-source-sftp";
    }
}
