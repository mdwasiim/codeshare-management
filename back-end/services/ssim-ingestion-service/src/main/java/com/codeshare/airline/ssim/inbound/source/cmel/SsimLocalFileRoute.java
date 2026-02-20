package com.codeshare.airline.ssim.inbound.source.cmel;

import com.codeshare.airline.ssim.inbound.domain.enums.SsimSourceType;
import com.codeshare.airline.ssim.inbound.orchestration.SsimProcessor;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SsimLocalFileRoute extends AbstractSsimSourceRoute {

    @Value("${ssim.source.local.path}")
    private String localPath;

    public SsimLocalFileRoute(SsimProcessor ssimProcessor) {
        super(ssimProcessor);
    }

    @Override
    protected String endpointUri() {
        return String.format(
                "file:%s"
                        + "?readLock=changed"
                        + "&readLockCheckInterval=2000"
                        + "&readLockTimeout=30000"
                        + "&move=.processed"
                        + "&moveFailed=.error"
                        + "&idempotent=true"
                        + "&idempotentKey=${file:name}-${file:size}-${file:modified}"
                        + "&idempotentRepository=#fileIdempotentRepo",
                localPath
        );
    }

    @Override
    protected SsimSourceType sourceType() {
        return SsimSourceType.LOCAL;
    }

    @Override protected String sourceSystem(Exchange exchange) {
        return exchange.getIn() .getHeader(Exchange.FILE_PARENT, String.class);
    }

    @Override
    protected String routeId() {
        return "ssim-source-local";
    }
}
