package com.thiago.imageprocessor.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Estructura estándar para respuestas de error en la API")
public class ErrorResponse {

    @Schema(description = "Mensaje detallado del error", example = "El usuario ya existe")
    private String mensaje;

    @Schema(description = "Código de estado HTTP", example = "400")
    private int statusCode;

    @Schema(description = "Nombre corto del error o categoría", example = "Bad Request")
    private String error;

    @Schema(description = "Fecha y hora en la que ocurrió el error", example = "2026-03-14T18:16:52")
    private LocalDateTime Timestamp;
}