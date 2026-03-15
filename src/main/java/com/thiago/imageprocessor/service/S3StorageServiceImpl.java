package com.thiago.imageprocessor.service;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;
import software.amazon.awssdk.core.sync.RequestBody;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3StorageServiceImpl implements StorageService {
    
    private final S3Client s3Client;
    
    // Es mejor leer el nombre del bucket desde properties
    private final String bucketName = "imageprocessor"; 

    public S3StorageServiceImpl(S3Client s3Client) {
        this.s3Client = s3Client;
    }
    // MÉTODO A: Para el archivo original (MultipartFile)
    @Override
    public String uploadFile(MultipartFile file) {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        try {
            s3Client.putObject(PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build(),
                RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            
            return String.format("http://localhost:4566/%s/%s", bucketName, fileName);
        } catch (IOException e) {
            throw new RuntimeException("Error al subir MultipartFile a S3", e);
        }
    }
    @Override
    public String uploadFile(java.awt.image.BufferedImage image, String fileName, String format) throws IOException {
        // 1. Convertir BufferedImage a bytes en memoria
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, format, baos);
        byte[] bytes = baos.toByteArray();

        String finalFileName = UUID.randomUUID() + "_" + fileName;

        try {
            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(finalFileName)
                            .contentType("image/" + format)
                            .build(),
                    RequestBody.fromBytes(bytes)); // Usamos fromBytes en lugar de InputStream

            return String.format("http://localhost:4566/%s/%s", bucketName, finalFileName);
        } catch (Exception e) {
            throw new RuntimeException("Error al subir imagen procesada a S3", e);
        }
    }

    @Override
    public void deleteFile(String fileName) {
        s3Client.deleteObject(d -> d.bucket(bucketName).key(fileName));
    }
    // --- NUEVO MÉTODO IMPLEMENTADO ---
    @Override
    public InputStream downloadFile(String fileName) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            // El SDK v2 devuelve un ResponseInputStream que extiende de InputStream
            return s3Client.getObject(getObjectRequest);
            
        } catch (Exception e) {
            throw new RuntimeException("Error al descargar el archivo desde S3: " + fileName, e);
        }
    }
}