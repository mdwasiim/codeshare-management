package com.codeshare.airline.common.auth.model;


import com.codeshare.airline.common.utils.mapper.audit.AuditBaseDto;
import lombok.Data;

@Data
public class PasswordChangeRequest extends AuditBaseDto {
    private String currentPassword;
    private String newPassword;

}
