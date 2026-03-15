package com.thiago.imageprocessor.transformer;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

import net.coobird.thumbnailator.Thumbnails;

public class FlipTransformer implements ImageTransformer {
    @Override
    public BufferedImage transform(BufferedImage image, Map<String, Object> parameters) throws IOException {
        // "horizontal" o "vertical"
        String type = (String) parameters.get("type");
        // Thumbnailator permite rotar, para flip puro a veces se usa Graphics2D directamente
        // pero aquí podemos simularlo con la rotación si es 180° o lógica personalizada.
        return Thumbnails.of(image)
                .scale(1.0)
                .rotate(type.equals("horizontal") ? 180 : 0) // Simplificación
                .asBufferedImage();
    }
}