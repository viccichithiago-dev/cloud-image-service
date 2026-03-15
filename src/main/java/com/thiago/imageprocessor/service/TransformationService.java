package com.thiago.imageprocessor.service;

import com.thiago.imageprocessor.dto.TransformationRequest;
import com.thiago.imageprocessor.dto.TransformationStep;
import com.thiago.imageprocessor.exception.InvalidImageFormatException;
import com.thiago.imageprocessor.exception.ImageNotFoundException;
import com.thiago.imageprocessor.model.Image;
import com.thiago.imageprocessor.model.User;
import com.thiago.imageprocessor.repository.ImageRepository;
import com.thiago.imageprocessor.service.StorageService;
import com.thiago.imageprocessor.transformer.ImageTransformer;
import com.thiago.imageprocessor.transformer.TransformerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

@Service
public class TransformationService {

    private final ImageRepository imageRepository;
    private final StorageService storageService;
    private final TransformerFactory transformerFactory;

    public TransformationService(ImageRepository imageRepository, StorageService storageService, TransformerFactory transformerFactory) {
        this.imageRepository = imageRepository;
        this.storageService = storageService;
        this.transformerFactory = transformerFactory;
    }

    @Transactional
    public Image transformImage(Long imageId, User user, TransformationRequest request) throws IOException, ImageNotFoundException {
        Image image = imageRepository.findByIdAndUsuario(imageId, user)
                .orElseThrow(() -> new ImageNotFoundException("Image not found with id: " + imageId));

        InputStream inputStream = storageService.downloadFile(image.getNombreArchivo());
        BufferedImage originalImage = ImageIO.read(inputStream);

        List<TransformationStep> steps = request.getSteps();
        for (TransformationStep step : steps) {
            ImageTransformer transformer = transformerFactory.getTransformer(step.getType());
            originalImage = transformer.transform(originalImage, step.getParameters());
        }

        String outputFormat = request.getOutputFormat() != null ? request.getOutputFormat() : image.getMimeType();
        String fileName = UUID.randomUUID() + "_" + hashSteps(steps) + "." + outputFormat;
        String transformedUrl = storageService.uploadFile(originalImage, fileName, outputFormat);

        image.getTransformedUrls().add(transformedUrl);
        imageRepository.save(image);

        return image;
    }

   private String hashSteps(List<TransformationStep> steps) {
    if (steps == null || steps.isEmpty()) return "original";

    return steps.stream()
            .map((TransformationStep step) -> {
                String type = step.getType();
                String params = String.valueOf(step.getParameters());
                return type + params;
            })
            .collect(Collectors.joining("_"));
}
}