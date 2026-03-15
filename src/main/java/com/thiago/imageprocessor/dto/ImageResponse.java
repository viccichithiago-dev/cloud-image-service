package com.thiago.imageprocessor.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.thiago.imageprocessor.model.ImageStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Detalles de la imagen procesada y sus metadatos")
public record ImageResponse(
    
    @Schema(description = "ID único de la imagen", example = "101")
    Long id,
    
    @Schema(description = "Nombre único asignado al archivo en el almacenamiento", example = "550e8400-e29b-41d4-a716.png")
    String nombreArchivo,
    
    @Schema(description = "Nombre original del archivo subido por el usuario", example = "vacaciones_2026.png")
    String originalNombreArchivo,
    
    @Schema(description = "URL pública de la imagen original", example = "https://s3.amazonaws.com/my-bucket/images/original.png")
    String url,
    
    @Schema(description = "Lista de URLs de las versiones transformadas (miniaturas, filtros, etc.)")
    List<String> transformedUrls,
    
    @Schema(description = "Tamaño del archivo en bytes", example = "2048576")
    Long size,
    
    @Schema(description = "Ancho de la imagen en píxeles", example = "1920")
    int width,
    
    @Schema(description = "Alto de la imagen en píxeles", example = "1080")
    int height,
    
    @Schema(description = "Tipo MIME del archivo", example = "image/png")
    String mimeType,
    
    @Schema(description = "Estado actual del procesamiento de la imagen", example = "PROCESSED")
    ImageStatus status,
    
    @Schema(description = "Fecha y hora de subida")
    LocalDateTime createdAt,
    
    @Schema(description = "ID del usuario propietario de la imagen", example = "1")
    Long userId
){}