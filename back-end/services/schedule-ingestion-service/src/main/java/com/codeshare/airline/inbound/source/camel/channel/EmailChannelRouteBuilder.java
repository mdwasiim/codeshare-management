/*
package com.codeshare.airline.ingestion.source.camel.channel;

import com.codeshare.airline.ingestion.domain.enums.SourceType;
import com.codeshare.airline.ingestion.persistence.entities.source.ScheduleIngestionChannelEntity;
import com.codeshare.airline.ingestion.source.security.ScheduleCredentialResolver;
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
    protected String buildUri(ScheduleIngestionChannelEntity c) {

        String password = resolvePassword(c);

        return String.format(
                "%s://%s/%s?username=%s&password=RAW(%s)"
                        + "&delay=%d"
                        + "&unseen=%s"
                        + "&delete=%s"
                        + "&peek=%s"
                        + "&moveTo=%s",
                c.getProtocol(),
                c.getHost(),
                c.getMailbox(),
                c.getUsername(),
                password,
                val(c.getMailDelayMs(), 60000),
                val(c.getMailUnseenOnly(), true),
                val(c.getMailDelete(), false),
                val(c.getMailPeek(), false),
                val(c.getMailMoveTo(), "Processed")
        );
    }

    @Override
    protected void validate(ScheduleIngestionChannelEntity c) {
        if (c.getProtocol() == null || c.getHost() == null
                || c.getMailbox() == null || c.getUsername() == null) {
            throw new IllegalStateException("Invalid EMAIL configuration");
        }
    }

    @Override
    protected void beforeProcessing(Exchange ex) throws Exception {

        var message = ex.getIn().getBody(jakarta.mail.Message.class);
        var content = message.getContent();

        if (content instanceof jakarta.mail.Multipart mp) {
            for (int i = 0; i < mp.getCount(); i++) {
                var part = mp.getBodyPart(i);
                if (part.getFileName() != null) {

                    ex.getMessage().setBody(part.getInputStream().readAllBytes());
                    ex.getMessage().setHeader("CamelFileName", part.getFileName());
                    return;
                }
            }
        }

        throw new IllegalStateException("No attachment found in email");
    }

    private String resolvePassword(ScheduleIngestionChannelEntity c) {
        return c.getPasswordEncrypted() != null
                ? resolver.decrypt(c.getPasswordEncrypted())
                : "";
    }
}
*/
