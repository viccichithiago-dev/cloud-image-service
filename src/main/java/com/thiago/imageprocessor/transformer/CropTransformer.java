package com.thiago.imageprocessor.transformer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

public class CropTransformer implements ImageTransformer {
    @Override
    public BufferedImage transform(BufferedImage image, Map<String, Object> parameters) throws IOException {
        int width = (int) parameters.get("width");
        int height = (int) parameters.get("height");
        // Por defecto centra el recorte, pero se podría parametrizar la posición
        return Thumbnails.of(image)
                .sourceRegion(Positions.CENTER, width, height)
                .size(width, height)
                .asBufferedImage();
    }
}