package com.thiago.imageprocessor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter 
@AllArgsConstructor
@Schema(description = "Información básica del perfil de usuario")
public class UserResponse {

    @Schema(description = "Identificador único del usuario", example = "1")
    private Long id;

    @Schema(description = "Nombre de usuario único en la plataforma", example = "thiago_dev")
    private String username;
}