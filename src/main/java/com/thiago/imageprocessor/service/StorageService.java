package com.thiago.imageprocessor.service;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
public interface StorageService {
    String uploadFile(MultipartFile file);
    void deleteFile(String fileName);
    InputStream downloadFile(String fileName);
}
