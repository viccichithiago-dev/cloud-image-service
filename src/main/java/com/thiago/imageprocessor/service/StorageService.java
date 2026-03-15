package com.thiago.imageprocessor.service;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;
public interface StorageService {
    String uploadFile(MultipartFile file);
    String uploadFile(java.awt.image.BufferedImage image, String fileName, String format) throws java.io.IOException;
    void deleteFile(String fileName);
    InputStream downloadFile(String fileName);
}
