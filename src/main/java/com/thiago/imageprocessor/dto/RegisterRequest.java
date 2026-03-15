package com.thiago.imageprocessor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Datos requeridos para crear una cuenta nueva en el sistema")
public class RegisterRequest {

    @NotBlank(message = "Error: el usuario no puede estar vacío")
    @Size(min = 3, max = 20, message = "Error: el usuario debe tener entre 3 y 20 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Error: el usuario solo puede contener letras y números")
    @Schema(
        description = "Nombre de usuario (alfanumérico, sin espacios)", 
        example = "thiago2026", 
        minLength = 3, 
        maxLength = 20,
        pattern = "^[a-zA-Z0-9]+$"
    )
    private String username;

    @NotBlank(message = "Error: la contraseña no puede estar vacía")
    @Size(min = 6, max = 20, message = "Error: la contraseña debe tener entre 6 y 20 caracteres")
    @Schema(
        description = "Contraseña segura para la cuenta", 
        example = "StrongPass123", 
        minLength = 6, 
        maxLength = 20
    )
    private String password;
}