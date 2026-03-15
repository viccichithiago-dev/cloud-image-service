package com.thiago.imageprocessor.dto;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class TransformationStep {
    @Schema(description = "Tipo de transformación", example = "ROTATE", allowableValues = {"RESIZE", "ROTATE", "CROP", "FILTER"})
    private String type;
    @Schema(description = "Mapa de parámetros específicos para la transformación", 
            example = "{\"angle\": 90.0}")
    private Map<String, Object> parameters;

    // Getters and Setters
}