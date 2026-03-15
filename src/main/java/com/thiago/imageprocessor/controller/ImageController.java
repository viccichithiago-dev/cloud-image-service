package com.thiago.imageprocessor.controller;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.thiago.imageprocessor.dto.ImageResponse;
import com.thiago.imageprocessor.dto.TransformationRequest;
import com.thiago.imageprocessor.exception.ImageNotFoundException;
import com.thiago.imageprocessor.model.Image;
import com.thiago.imageprocessor.model.User;
import com.thiago.imageprocessor.service.ImageService;
import com.thiago.imageprocessor.service.TransformationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
@Tag(name = "Imágenes", description = "Endpoints para gestión y transformación de imágenes")
@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;
    private final TransformationService transformationService;

    public ImageController(ImageService imageService,TransformationService transformationService) {
        this.imageService = imageService;
        this.transformationService = transformationService;
    }

    // 1. Subida de Imagen
    @Operation(
        summary = "Subir una nueva imagen",
        description = "Carga un archivo de imagen al servidor y lo almacena en S3/LocalStack.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Imagen subida exitosamente"),
        @ApiResponse(responseCode = "400", description = "Formato de archivo no válido"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<ImageResponse> uploadImage(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user) { // Spring Security ya sabe quién es el usuario
        
        return new ResponseEntity<>(imageService.uploadImage(user, file), HttpStatus.CREATED);
    }

    // 2. Obtener una imagen específica
    @Operation(
        summary = "Obtener detalles de una imagen",
        description = "Devuelve la información técnica y la URL de una imagen perteneciente al usuario autenticado.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Detalles encontrados"),
        @ApiResponse(responseCode = "404", description = "Imagen no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ImageResponse> getImage(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        
        return ResponseEntity.ok(imageService.getImage(user, id));
    }

    // 3. Eliminar una imagen
    @Operation(
        summary = "Eliminar imagen",
        description = "Borra físicamente el archivo de S3 y elimina el registro de la base de datos.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        
        imageService.deleteImage(user, id);
        return ResponseEntity.noContent().build();
    }
    // 4. Descargar imagen original o transformada desde S3
    @Operation(
        summary = "Descargar archivo de imagen",
        description = "Obtiene el flujo de bytes de la imagen original almacenada.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Archivo descargado",
            content = @Content(mediaType = "application/octet-stream")
        ),
        @ApiResponse(responseCode = "404", description = "Imagen no encontrada")
    })
    @GetMapping(value = "/{id}/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> downloadImage(
            @PathVariable Long id,
            @AuthenticationPrincipal User user,
            HttpServletResponse response) throws ImageNotFoundException {
        Image image = imageService.findImageByIdAndUser(id, user);
        byte[] imageBytes = imageService.downloadImage(image);

        response.setHeader("Content-Disposition", "attachment; filename=\"" + image.getNombreArchivo() + "\"");
        response.setContentType(image.getMimeType());
        response.setContentLength(imageBytes.length);

        return ResponseEntity.ok(imageBytes);
    }
    

    // 5. Listar imágenes del usuario autenticado con paginación
    @Operation(
        summary = "Transformar una imagen",
        description = "Aplica una lista de transformaciones (rotar, redimensionar, etc.) a una imagen existente.",
        security = @SecurityRequirement(name = "Bearer Authentication")
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transformación exitosa"),
        @ApiResponse(responseCode = "404", description = "Imagen no encontrada"),
        @ApiResponse(responseCode = "401", description = "Token no válido o ausente")
    })
    @GetMapping
    public ResponseEntity<Page<ImageResponse>> getImages(
            @AuthenticationPrincipal User user,
            Pageable pageable) {
        
        Page<Image> images = imageService.getImagesByUser(user, pageable);
        
        // Pasamos el usuario autenticado y el ID de la imagen actual en el map
        Page<ImageResponse> imageResponses = images.map(image -> 
            imageService.getImageResponse(user, image.getId())
        );
        
        return ResponseEntity.ok(imageResponses);
    }

    // 6. Transformar una imagen
    @PostMapping("/{id}/transform")
    public ResponseEntity<ImageResponse> transformImage(
            @PathVariable Long id,
            @AuthenticationPrincipal User user,
            @RequestBody TransformationRequest request) throws IOException, ImageNotFoundException {
        Image transformedImage = transformationService.transformImage(id, user, request);
        return ResponseEntity.ok(imageService.getImageResponse(user, transformedImage.getId()));
    }
}
