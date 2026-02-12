package com.codeshare.airline.ssim.validation.rule;

import com.codeshare.airline.ssim.validation.context.ValidationContext;

public interface RecordRule {
    void validate(String line, ValidationContext ctx);
}
