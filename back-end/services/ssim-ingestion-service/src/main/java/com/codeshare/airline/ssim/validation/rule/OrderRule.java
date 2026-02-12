package com.codeshare.airline.ssim.validation.rule;

import com.codeshare.airline.ssim.validation.context.ValidationContext;

public interface OrderRule {
    public void validate(String line, ValidationContext ctx);
}
