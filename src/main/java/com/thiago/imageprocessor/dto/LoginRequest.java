package com.thiago.imageprocessor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Credenciales necesarias para autenticar a un usuario")
public class LoginRequest {

    @NotBlank(message = "Error: el usuario no puede estar vacío")
    @Size(min = 3, max = 20, message = "Error: el usuario debe tener entre 3 y 20 caracteres")
    @Schema(description = "Nombre de usuario registrado", example = "thiago_dev", minLength = 3, maxLength = 20)
    private String username;

    @NotBlank(message = "Error: la contraseña no puede estar vacía")
    @Size(min = 6, max = 20, message = "Error: la contraseña debe tener entre 6 y 20 caracteres")
    @Schema(description = "Contraseña del usuario", example = "Password123!", minLength = 6, maxLength = 20)
    private String password;
}