package com.thiago.imageprocessor.dto;

import java.time.LocalDateTime;

import com.thiago.imageprocessor.model.ImageStatus;

public record ImageResponse(
    Long id,
    String nombreArchivo,
    String originalNombreArchivo,
    String url,
    Long size,
    int width,
    int height,
    String mimeType,
    ImageStatus status,
    LocalDateTime createdAt,
    Long userId // Solo enviamos el ID del usuario, no el objeto completo
) {}