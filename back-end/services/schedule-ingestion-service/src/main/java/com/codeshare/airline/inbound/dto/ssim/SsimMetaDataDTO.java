package com.codeshare.airline.inbound.dto.ssim;

import com.codeshare.airline.inbound.dto.schedule.ScheduleFileMetaDataDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.InputStream;
import java.util.function.Supplier;

@Getter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "streamSupplier")
public class SsimMetaDataDTO extends ScheduleFileMetaDataDTO {

    // SSIM-specific fields
    private String externalReference;

    private Integer totalRecordCount;

    private Supplier<InputStream> streamSupplier;

    // =========================
    // STREAM PROCESSING
    // =========================
    public InputStream openStream() {
        if (streamSupplier == null) {
            throw new IllegalStateException(
                    "streamSupplier is null | fileName=" + getFileName());
        }

        InputStream stream = streamSupplier.get();

        if (stream == null) {
            throw new IllegalStateException("Stream supplier returned null");
        }

        return stream;
    }

}