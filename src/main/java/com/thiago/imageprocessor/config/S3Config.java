package com.thiago.imageprocessor.config;

import java.net.URI;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;

@Configuration
public class S3Config {

    @Bean
    public S3Client s3Client() {
        // 1. Construimos el cliente primero
        S3Client client = S3Client.builder()
                .endpointOverride(URI.create("http://localhost:4566"))
                .region(Region.US_EAST_1)
                .forcePathStyle(true)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create("test", "test")))
                .build();

        // 2. Usamos la variable 'client' para crear el bucket automáticamente
        try {
            String bucketName = "imageprocessor";
            // Verificamos si ya existe para evitar excepciones molestas en la consola
            try {
                client.headBucket(HeadBucketRequest.builder().bucket(bucketName).build());
            } catch (Exception e) {
                client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
                System.out.println("✅ Bucket '" + bucketName + "' creado exitosamente en LocalStack.");
            }
        } catch (Exception e) {
            System.err.println("❌ No se pudo conectar con LocalStack. ¿Está Docker encendido?");
        }

        return client;
    }
}