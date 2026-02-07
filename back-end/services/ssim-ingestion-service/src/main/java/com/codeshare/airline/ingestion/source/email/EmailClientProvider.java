package com.codeshare.airline.ingestion.source.email;

import com.codeshare.airline.ingestion.config.SourceProperties;
import jakarta.mail.Session;
import jakarta.mail.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@RequiredArgsConstructor
public class EmailClientProvider {

    private final SourceProperties props;

    public Store connect() throws Exception {

        Properties p = new Properties();
        p.put("mail.store.protocol", "imaps");

        Session session = Session.getInstance(p);
        Store store = session.getStore();

        store.connect(
                props.getEmail().getHost(),
                props.getEmail().getUser(),
                props.getEmail().getPassword()
        );

        return store;
    }
}
