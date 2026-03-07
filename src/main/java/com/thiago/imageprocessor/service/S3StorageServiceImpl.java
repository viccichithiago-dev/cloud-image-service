package com.thiago.imageprocessor.service;
import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class S3StorageServiceImpl implements StorageService{
    private final S3Client s3Client;
    private final String bucketName= "nombre-bucket";

    public S3StorageServiceImpl(S3Client s3Client){
        this.s3Client= s3Client;
    }

    @Override
    public String uploadFile(MultipartFile file) {
        // Generar un nombre único para evitar colisiones
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        
        try {
            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileName)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            
            // Retornar la URL (esto depende de tu configuración de S3)
            return String.format("https://%s.s3.amazonaws.com/%s", bucketName, fileName);
        } catch (IOException e) {
            throw new RuntimeException("Error al subir archivo a S3", e);
        }
    }

    @Override
    public void deleteFile(String fileName) {
        // Lógica para borrar en S3
    }

}
