package com.codeshare.airline.ssim.inbound.parsing.splitter;

import com.codeshare.airline.ssim.inbound.domain.contex.FlightBlockContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@Slf4j
public class FlightBlockSplitter {

    private FlightBlockContext current;

    public void accept(String line, Consumer<FlightBlockContext> consumer) {

        if (line == null || line.isBlank()) return;

        line = line.replaceAll("^[\\r\\n]+", "");
        char type = line.charAt(0);

        switch (type) {

            case '3' -> {
                flush(consumer);

                current = createBlockFromT3(line);
                current.addLine(line);
            }

            case '4' -> {
                if (current == null) {
                    throw new IllegalStateException("T4 encountered before T3");
                }
                current.addLine(line);
            }

            default -> {
                // ignore other types
            }
        }
    }

    public void flushRemaining(Consumer<FlightBlockContext> consumer) {
        flush(consumer);
    }

    private void flush(Consumer<FlightBlockContext> consumer) {
        if (current != null) {
            consumer.accept(current);
            current = null;
        }
    }

    private FlightBlockContext createBlockFromT3(String line) {

        String carrier = line.substring(2, 5).trim();
        String flightNumber = line.substring(5, 9).trim();

        return new FlightBlockContext(flightNumber, carrier);
    }
}

