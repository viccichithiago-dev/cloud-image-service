package com.thiago.imageprocessor.service;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.thiago.imageprocessor.dto.ImageResponse;
import com.thiago.imageprocessor.exception.ImageNotFoundException;
import com.thiago.imageprocessor.exception.InvalidImageFormatException;
import com.thiago.imageprocessor.model.Image;
import com.thiago.imageprocessor.model.ImageStatus;
import com.thiago.imageprocessor.model.User;
import com.thiago.imageprocessor.repository.ImageRepository;
import com.thiago.imageprocessor.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
@Service
public class ImageService {
    
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final StorageService storageService; // Tu S3StorageServiceImpl

    public ImageService(ImageRepository imageRepository, UserRepository userRepository,StorageService storageService) {
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
        this.storageService = storageService;
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
    public ImageResponse uploadImage(User user, MultipartFile file) {
        try {
            // Validar que sea una imagen
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new InvalidImageFormatException("El archivo debe ser una imagen válida");
            }
            
            // 1. Extraer dimensiones
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();

            // 2. Subir a LocalStack/S3 y obtener la URL
            String url = storageService.uploadFile(file);

            // --- EL CAMBIO ESTÁ ACÁ ---
            // Extraemos el nombre real (el que tiene el UUID) de la URL
            // Si la URL es: http://localhost:4566/imageprocessor/uuid_foto.png
            // nombreReal será: uuid_foto.png
            String nombreRealEnS3 = url.substring(url.lastIndexOf("/") + 1);

            // 3. Crear entidad Image
            Image image = Image.builder()
                    .nombreArchivo(nombreRealEnS3) // <--- USAMOS EL NOMBRE CON UUID
                    .originalNombreArchivo(file.getOriginalFilename()) // Guardamos el original para el usuario
                    .url(url)
                    .size(file.getSize())
                    .width(width)
                    .height(height)
                    .mimeType(file.getContentType())
                    .status(ImageStatus.PROCESADO)
                    .usuario(user)
                    .build();

            // 4. Guardar y retornar DTO
            return mapToResponse(imageRepository.save(image));

        } catch (IOException e) {
            throw new RuntimeException("Error al procesar la imagen", e);
        }
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
    /**
     * Elimina una imagen específica.
     *
     * @param user   El usuario que realizó la solicitud.
     * @param id     El ID de la imagen a eliminar.
     */
    @Transactional
    public void deleteImage(User user, Long id) {
        Image image = findImageOrThrow(id, user);
        // 1. Borrar de S3/LocalStack
        storageService.deleteFile(image.getNombreArchivo());
        
        // 2. Borrar de la BD
        imageRepository.delete(image);
    }
    public Page<Image> getImagesByUser(User user, Pageable pageable) {
        // Retornamos la página directamente desde el repo
        return imageRepository.findByUsuario(user, pageable);
    }
    /**
     * Busca una imagen por su ID y asegura que pertenezca al usuario dado.
     *
     * @param id     El ID de la imagen.
     * @param user   El usuario que debe poseer la imagen.
     * @return       La imagen encontrada.
     * @throws ImageNotFoundException Si la imagen no se encuentra o no pertenece al usuario.
     */
    @Transactional(readOnly = true)
    public Image findImageByIdAndUser(Long id, User user) throws ImageNotFoundException {
        Optional<Image> optionalImage = imageRepository.findByIdAndUsuario(id, user);
        return optionalImage.orElseThrow(() -> new ImageNotFoundException("Imagen no encontrada con ID: " + id));
    }
     /**
     * Descarga la imagen original o transformada desde S3.
     *
     * @param image  La imagen a descargar.
     * @return       Los bytes de la imagen.
     * @throws IOException Si ocurre un error al descargar la imagen.
     */
    @Transactional(readOnly = true)
    public byte[] downloadImage(Image image) { // <--- QUITAMOS EL THROWS
        try (InputStream inputStream = storageService.downloadFile(image.getNombreArchivo());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            
            inputStream.transferTo(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
            
        } catch (IOException e) {
            // Al lanzarlo como RuntimeException, el error "sube" solo sin romper la firma
            throw new RuntimeException("Error técnico al leer los bytes de la imagen", e);
        }
    }
}