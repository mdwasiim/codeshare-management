package com.codeshare.airline.schedule.ingestion.source.camel.channel;

import com.codeshare.airline.platform.core.enums.schedule.SourceType;
import com.codeshare.airline.schedule.ingestion.dto.source.AirlineIngestionChannelDTO;
import com.codeshare.airline.schedule.ingestion.source.security.ScheduleCredentialResolver;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.Part;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailChannelRouteBuilder extends AbstractChannelRouteBuilder {

    private final ScheduleCredentialResolver resolver;

    @Override
    public SourceType supports() {
        return SourceType.EMAIL;
    }

    @Override
    protected String buildUri(AirlineIngestionChannelDTO c) {

        String password = resolvePassword(c);

        return new StringBuilder()
                .append(c.getProtocol())
                .append("://")
                .append(c.getHost())
                .append("/")
                .append(c.getMailbox())
                .append("?username=").append(c.getUsername())
                .append("&password=RAW(").append(password).append(")")
                .append("&delay=").append(val(c.getMailDelayMs(), 60000))
                .append("&unseen=").append(val(c.getMailUnseenOnly(), true))
                .append("&delete=").append(val(c.getMailDelete(), false))
                .append("&peek=").append(val(c.getMailPeek(), false))
                .append("&moveTo=").append(val(c.getMailMoveTo(), "Processed"))
                .append("&bridgeErrorHandler=").append(val(c.getBridgeErrorHandler(), true))
                .toString();
    }

    @Override
    protected void validate(AirlineIngestionChannelDTO c) {
        if (c.getProtocol() == null || c.getProtocol().isBlank()) {
            throw new IllegalStateException("Email protocol is required");
        }
        if (c.getHost() == null || c.getHost().isBlank()) {
            throw new IllegalStateException("Email host is required");
        }
        if (c.getMailbox() == null || c.getMailbox().isBlank()) {
            throw new IllegalStateException("Email mailbox is required");
        }
        if (c.getUsername() == null || c.getUsername().isBlank()) {
            throw new IllegalStateException("Email username is required");
        }
    }

    @Override
    protected void beforeProcessing(Exchange exchange) throws Exception {

        Message message = exchange.getMessage().getBody(Message.class);
        if (message == null) {
            throw new IllegalStateException("Email message body is missing");
        }

        Object content = message.getContent();
        if (content instanceof Multipart multipart) {
            extractFirstAttachment(exchange, multipart);
            return;
        }

        throw new IllegalStateException("No attachment found in email");
    }

    private void extractFirstAttachment(Exchange exchange, Multipart multipart) throws Exception {
        for (int i = 0; i < multipart.getCount(); i++) {
            Part part = multipart.getBodyPart(i);
            String fileName = part.getFileName();

            if (fileName != null && !fileName.isBlank()) {
                exchange.getMessage().setBody(part.getInputStream().readAllBytes());
                exchange.getMessage().setHeader("CamelFileName", fileName);
                return;
            }
        }

        throw new IllegalStateException("No attachment found in email");
    }

    private String resolvePassword(AirlineIngestionChannelDTO c) {
        return c.getPasswordEncrypted() != null && !c.getPasswordEncrypted().isBlank()
                ? resolver.decrypt(c.getPasswordEncrypted())
                : "";
    }
}
