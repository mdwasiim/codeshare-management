package com.codeshare.airline.core.dto.auth;

import com.codeshare.airline.core.dto.audit.dto.CSMAuditableDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AuthPasswordChangeRequest extends CSMAuditableDTO {
    private String currentPassword;
    private String newPassword;
}
