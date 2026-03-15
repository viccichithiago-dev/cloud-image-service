package com.thiago.imageprocessor.transformer;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

import net.coobird.thumbnailator.Thumbnails;
public class FormatChangeTransformer implements ImageTransformer {
    @Override
    public BufferedImage transform(BufferedImage image, Map<String, Object> parameters) throws IOException {
        String format = (String) parameters.get("format"); // Ej: "png", "jpg"
        return Thumbnails.of(image)
                .scale(1.0)
                .outputFormat(format)
                .asBufferedImage();
    }
}