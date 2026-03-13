package com.thiago.imageprocessor.service;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

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
            
            // IMPORTANTE: En LocalStack la URL es diferente a la de AWS real
            return String.format("http://localhost:4566/%s/%s", bucketName, fileName);
            
        } catch (IOException e) {
            throw new RuntimeException("Error al subir archivo a S3", e);
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