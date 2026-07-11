package com.codeshare.airline.platform.core.dto.auth;

import com.codeshare.airline.platform.core.dto.audit.CSMAuditableDTO;
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
