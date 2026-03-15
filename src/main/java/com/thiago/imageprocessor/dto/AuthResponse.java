package com.thiago.imageprocessor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Schema(description = "Respuesta de autenticación que contiene los datos del usuario y el token de acceso")
public class AuthResponse {
    @Schema(description = "ID único del usuario en la base de datos", example = "1")
    private Long id;
    @Schema(description = "Nombre de usuario único", example = "thiago_dev")
    private String username;
    @Schema(description = "Token JWT para autenticar peticiones posteriores", 
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;  // JWT token
    
    // Constructor opcional para facilitar creación
    public AuthResponse(Long id, String username, String token) {
        this.id = id;
        this.username = username;
        this.token = token;
    }
}