package com.codeshare.airline.ssim.source.cmel;

import com.codeshare.airline.ssim.processor.SsimProcessor;
import com.codeshare.airline.ssim.source.SsimSourceType;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

@Component
public class SsimSftpRoute extends AbstractSsimSourceRoute {

    public SsimSftpRoute(SsimProcessor ssimProcessor) {
        super(ssimProcessor);
    }

    @Override
    protected String endpointUri() {
        return "sftp://sftpUser@host/ssim"
                + "?password=secret"
                + "&binary=true"
                + "&stepwise=false"
                + "&disconnect=true"
                + "&readLock=changed"
                + "&move=.processed"
                + "&moveFailed=.error"
                + "&idempotent=true"
                + "&idempotentKey=${file:name}-${file:size}-${file:modified}"
                + "&idempotentRepository=#fileIdempotentRepo";
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
