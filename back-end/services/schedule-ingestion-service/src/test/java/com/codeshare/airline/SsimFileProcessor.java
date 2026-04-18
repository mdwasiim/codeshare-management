package com.codeshare.airline;

import com.codeshare.airline.core.enums.MessageType;
import com.codeshare.airline.inbound.domain.context.ScheduleGroupedMessage;
import com.codeshare.airline.inbound.orchestration.parsers.SsimMessageParser;
import com.codeshare.airline.inbound.dto.common.ssim.SsimFlightDTO;
import com.codeshare.airline.inbound.dto.ssim.SSIMMessageDTO;
import com.codeshare.airline.inbound.stream.extractor.SsimMessageExtractor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class SsimFileProcessor {

    private static final Logger log = LogManager.getLogger(SsimFileProcessor.class);

    static int count =0;
    public static void main(String[] args) throws Exception {


        Path filePath = Path.of("C:\\Users\\mdwas\\Projects\\codeBase\\codeshare-management\\back-end\\services\\schedule-ingestion-service\\src\\main\\resources\\SSIM_FULL.txt");

        System.out.println("=========== SSIM BENCHMARK ===========");

        //runSingleThread(filePath);

        System.out.println("\n--------------------------------------\n");

        runMultiThread(filePath);
    }

    /* ================= SINGLE THREAD ================= */

    private static void runSingleThread(Path filePath) throws IOException {

        SsimMessageExtractor extractor = new SsimMessageExtractor(MessageType.SSIM);

        AtomicInteger flightCount = new AtomicInteger();
        AtomicInteger fileCount = new AtomicInteger();

        long start = System.currentTimeMillis();

        try (InputStream is = Files.newInputStream(filePath)) {

            extractor.extract(is, block -> {

                if (isFlightBlock(block)) {
                    flightCount.incrementAndGet();
                } else {
                    fileCount.incrementAndGet();
                }

                // simulate processing
                processBlock(block);
            });
        }

        long end = System.currentTimeMillis();

        printSummary(" SINGLE THREAD", flightCount, fileCount, end - start);
    }

    /* ================= MULTI THREAD ================= */

    private static void runMultiThread(Path filePath) throws Exception {

        SsimMessageExtractor extractor = new SsimMessageExtractor(MessageType.SSIM);

        int threads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        AtomicInteger flightCount = new AtomicInteger();
        AtomicInteger fileCount = new AtomicInteger();

        long start = System.currentTimeMillis();

        try (InputStream is = Files.newInputStream(filePath)) {

            extractor.extract(is, block -> {

                if (isFlightBlock(block)) {

                    flightCount.incrementAndGet();

                    // 🔥 process flight in parallel
                    executor.submit(() -> processBlock(block));

                } else {
                    fileCount.incrementAndGet();

                    // file-level can be processed immediately
                    processBlock(block);
                }
            });
        }

        // wait for all tasks
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.MINUTES);

        long end = System.currentTimeMillis();

        printSummary(" MULTI THREAD (" + threads + " threads)", flightCount, fileCount, end - start);
    }

    /* ================= PROCESS ================= */

    private static void processBlock(List<String> lines) {

        if (lines.isEmpty()) return;

        try {

            SsimMessageParser ssimMessageParser = new SsimMessageParser();
            ScheduleGroupedMessage scheduleGroupedMessage =
                    new ScheduleGroupedMessage(null, null, lines);

            SSIMMessageDTO dto = ssimMessageParser.parseMessage(scheduleGroupedMessage);

            log.info("==============================");
            log.info("SSIM PARSED OBJECT");
            log.info("==============================");

            // 🔹 Header
            if (dto.getHeader() != null) {
                log.info(" HEADER → {}", dto.getHeader());
            }

            // 🔹 Carrier
            if (dto.getCarrier() != null) {
                log.info(" CARRIER → {}", dto.getCarrier());
            }

            // 🔹 Flight Info
            log.info("Flight → {}{}", dto.getAirlineCode(), dto.getFlightNumber());

            // 🔹 Legs
            if (dto.hasFlights()) {
                log.info(" LEGS:");

                int i = 1;
                for (SsimFlightDTO leg : dto.getFlights()) {
                    log.info("   Leg {}: {}", i++, leg);
                }
            } else {
                log.warn(" No Legs Found");
            }

            // 🔹 Trailer
            if (dto.getTrailer() != null) {
                log.info(" TRAILER → {}", dto.getTrailer());
            }

        } catch (Exception e) {
            log.error(" Failed to process SSIM block: {}", lines, e);
        }
    }

    /* ================= SUMMARY ================= */

    private static void printSummary(String mode,
                                     AtomicInteger flightCount,
                                     AtomicInteger fileCount,
                                     long timeMs) {

        System.out.println(mode);
        System.out.println("==============================");
        System.out.println(" Flights          : " + flightCount.get());
        System.out.println(" File Records     : " + fileCount.get());
        System.out.println(" Time (ms)        : " + timeMs);
        System.out.println(" Time (sec)       : " + (timeMs / 1000.0));

        if (flightCount.get() > 0) {
            System.out.println(" Avg/Flight (ms)  : " + (timeMs / flightCount.get()));
        }
    }

    /* ================= HELPERS ================= */

    private static boolean isFlightBlock(List<String> lines) {
        return !lines.isEmpty() && lines.getFirst().trim().startsWith("3");
    }

    private static String extractFlightKey(String line) {
        try {
            String content = line.substring(1);

            String airline = safeSubstring(content, 0, 3).trim();
            String flight = safeSubstring(content, 3, 8).trim();
            String suffix = safeSubstring(content, 8, 10).trim();

            return airline + flight + suffix;
        } catch (Exception e) {
            return "UNKNOWN";
        }
    }

    private static String safeSubstring(String s, int start, int end) {
        if (s.length() >= end) return s.substring(start, end);
        if (s.length() > start) return s.substring(start);
        return "";
    }
}