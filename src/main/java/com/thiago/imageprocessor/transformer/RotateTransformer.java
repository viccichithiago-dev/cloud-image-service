package com.thiago.imageprocessor.transformer;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

import net.coobird.thumbnailator.Thumbnails;

public class RotateTransformer implements ImageTransformer {
    @Override
    public BufferedImage transform(BufferedImage image, Map<String, Object> parameters) throws IOException {
        double angle = (double) parameters.get("angle");
        return Thumbnails.of(image)
                .scale(1.0) // Mantiene el tamaño original
                .rotate(angle)
                .asBufferedImage();
    }
}