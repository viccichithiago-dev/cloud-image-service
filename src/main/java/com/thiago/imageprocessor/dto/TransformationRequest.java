package com.thiago.imageprocessor.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Solicitud para aplicar una serie de transformaciones a una imagen")
public class TransformationRequest {

    @Schema(description = "Lista secuencial de pasos de transformación (resize, filter, etc.)")
    private List<TransformationStep> steps;

    @Schema(
        description = "Formato de salida deseado para la imagen procesada", 
        example = "webp", 
        allowableValues = {"png", "jpg", "jpeg", "webp"}
    )
    private String outputFormat;
}