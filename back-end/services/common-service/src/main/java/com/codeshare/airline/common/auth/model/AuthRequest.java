package com.codeshare.airline.common.auth.model;


import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}
