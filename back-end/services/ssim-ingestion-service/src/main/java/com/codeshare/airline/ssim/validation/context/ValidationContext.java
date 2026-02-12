package com.codeshare.airline.ssim.validation.context;

import com.codeshare.airline.ssim.validation.severity.ValidationMessage;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ValidationContext {

    private boolean t1Seen;
    private boolean t2Seen;
    private boolean t3Seen;
    private boolean t5Seen;

    private long recordsRead;

    // ⚠️ WARN messages only (bounded)
    private final List<ValidationMessage> warnings =
            new ArrayList<>();

    public void warn(String message) {
        warnings.add(
                new ValidationMessage(
                        com.codeshare.airline.ssim.validation.severity.ValidationSeverity.WARN,
                        message
                )
        );
    }
}
