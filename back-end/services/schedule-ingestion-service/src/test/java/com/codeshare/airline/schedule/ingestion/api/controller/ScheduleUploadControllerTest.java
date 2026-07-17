package com.codeshare.airline.schedule.ingestion.api.controller;

import com.codeshare.airline.schedule.ingestion.application.ingest.ScheduleMessageTypeResolver;
import org.apache.camel.ProducerTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ScheduleUploadControllerTest {

    @Test
    void uploadRoutesInferredSsimMessagesToSsimPipeline() throws Exception {
        ProducerTemplate producerTemplate = mock(ProducerTemplate.class);
        ScheduleUploadController controller = new ScheduleUploadController(
                producerTemplate,
                new ScheduleMessageTypeResolver()
        );

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "winter-2026.txt",
                "text/plain",
                "SSIM\n1HEADER\n".getBytes()
        );

        controller.upload(file, "QR", null);

        verify(producerTemplate).sendBodyAndHeaders(
                eq("seda:ssim-dataset-processing"),
                any(byte[].class),
                argThat(headers -> "SSIM".equals(headers.get("MESSAGE_TYPE")))
        );
    }
}
