package com.codeshare.airline.common.auth.model;

import com.codeshare.airline.common.audit.AuditBaseDto;
import lombok.Data;

@Data
public class RefreshTokenRequest extends AuditBaseDto {
    private String refreshToken;


}
