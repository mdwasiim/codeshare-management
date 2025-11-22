package com.codeshare.airline.common.auth.model;


import lombok.Data;

@Data
public class PasswordChangeRequest {
    private String currentPassword;
    private String newPassword;
}
