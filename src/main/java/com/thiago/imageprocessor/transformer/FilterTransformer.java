package com.thiago.imageprocessor.transformer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

import net.coobird.thumbnailator.Thumbnails;
public class FilterTransformer implements ImageTransformer {
    @Override
    public BufferedImage transform(BufferedImage image, Map<String, Object> parameters) throws IOException {
        // Ejemplo simple de Grayscale
        return Thumbnails.of(image)
                .scale(1.0)
                .imageType(BufferedImage.TYPE_BYTE_GRAY)
                .asBufferedImage();
    }
}