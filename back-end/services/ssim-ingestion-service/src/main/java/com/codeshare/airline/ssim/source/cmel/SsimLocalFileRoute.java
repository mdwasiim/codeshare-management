package com.codeshare.airline.ssim.source.cmel;

import com.codeshare.airline.ssim.processor.SsimProcessor;
import com.codeshare.airline.ssim.source.SsimSourceType;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

@Component
public class SsimLocalFileRoute extends AbstractSsimSourceRoute {

    public SsimLocalFileRoute(SsimProcessor ssimProcessor) {
        super(ssimProcessor);
    }

    @Override
    protected String endpointUri() {
        return "file:/opt/ssim/inbound"
                + "?readLock=changed"
                + "&readLockCheckInterval=2000"
                + "&readLockTimeout=30000"
                + "&move=.processed"
                + "&moveFailed=.error"
                + "&idempotent=true"
                + "&idempotentKey=${file:name}-${file:size}-${file:modified}"
                + "&idempotentRepository=#fileIdempotentRepo";
    }

    @Override
    protected SsimSourceType sourceType() {
        return SsimSourceType.LOCAL;
    }

    @Override
    protected String sourceSystem(Exchange exchange) {
        return exchange.getIn()
                .getHeader(Exchange.FILE_PARENT, String.class);
    }

    @Override
    protected String routeId() {
        return "ssim-source-local";
    }
}

