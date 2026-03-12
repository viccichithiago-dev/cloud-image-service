package com.thiago.imageprocessor.dto;

import java.time.LocalDateTime;

import com.thiago.imageprocessor.model.ImageStatus;

import lombok.Builder;

@Builder // Suficiente para crear instancias fluidas
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
    Long userId
){}