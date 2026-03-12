package com.thiago.imageprocessor.service;

import org.springframework.stereotype.Service;

import com.thiago.imageprocessor.dto.ImageResponse;
import com.thiago.imageprocessor.model.Image;
import com.thiago.imageprocessor.model.User;
import com.thiago.imageprocessor.repository.ImageRepository;
import com.thiago.imageprocessor.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    public ImageService(ImageRepository imageRepository, UserRepository userRepository) {
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
    }

    /**
     * Obtiene una imagen específica convertida a ImageResponse.
     */
    public ImageResponse getImageResponse(User user, Long id) {
        Image image = findImageOrThrow(id, user);
        return mapToResponse(image);
    }

    /**
     * Método privado para reutilizar la lógica de búsqueda y validación de propiedad.
     */
    private Image findImageOrThrow(Long id, User user) {
        return imageRepository.findByIdAndUsuario(id, user)
            .orElseThrow(() -> new RuntimeException("Image not found with id: " + id));
    }

    /**
     * Centraliza el mapeo de Entidad a Record DTO.
     */
    private ImageResponse mapToResponse(Image image) {
        return ImageResponse.builder()
            .id(image.getId())
            .nombreArchivo(image.getNombreArchivo())
            .originalNombreArchivo(image.getOriginalNombreArchivo())
            .url(image.getUrl())
            .size(image.getSize())
            .width(image.getWidth())
            .height(image.getHeight())
            .mimeType(image.getMimeType())
            .status(image.getStatus())
            .createdAt(image.getCreatedAt())
            .userId(image.getUsuario().getId())
            .build();
    }

    @Transactional
    public ImageResponse updateImage(User user, Long id, Image updatedImage) {
        Image image = findImageOrThrow(id, user);

        image.setNombreArchivo(updatedImage.getNombreArchivo());
        image.setOriginalNombreArchivo(updatedImage.getOriginalNombreArchivo());
        image.setUrl(updatedImage.getUrl());
        image.setSize(updatedImage.getSize());
        image.setWidth(updatedImage.getWidth());
        image.setHeight(updatedImage.getHeight());
        image.setMimeType(updatedImage.getMimeType());
        image.setStatus(updatedImage.getStatus());

        return mapToResponse(imageRepository.save(image));
    }
    public ImageResponse getImage(User user, Long id){
       // 1. Usamos el repositorio para buscar por ID y Usuario (Seguridad)
    Image image = imageRepository.findByIdAndUsuario(id, user)
        .orElseThrow(() -> new RuntimeException("No se encontró la imagen con ID: " + id));

        // 2. Mapeamos la entidad al Record DTO usando el Builder
        return mapToResponse(image);
    }
}