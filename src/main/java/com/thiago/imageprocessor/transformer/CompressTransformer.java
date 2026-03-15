package com.thiago.imageprocessor.transformer;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

import net.coobird.thumbnailator.Thumbnails;
public class CompressTransformer implements ImageTransformer {
    @Override
    public BufferedImage transform(BufferedImage image, Map<String, Object> parameters) throws IOException {
        float quality = (float) parameters.get("quality"); // Ej: 0.8f
        return Thumbnails.of(image)
                .scale(1.0)
                .outputQuality(quality)
                .asBufferedImage();
    }
}