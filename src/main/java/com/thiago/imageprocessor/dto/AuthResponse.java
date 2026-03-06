package com.thiago.imageprocessor.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
    
    private Long id;
    private String username;
    private String token;  // JWT token
    
    // Constructor opcional para facilitar creación
    public AuthResponse(Long id, String username, String token) {
        this.id = id;
        this.username = username;
        this.token = token;
    }
}