package com.thiago.imageprocessor.transformer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

import net.coobird.thumbnailator.Thumbnails;

public class ResizeTransformer implements ImageTransformer {
    @Override
    public BufferedImage transform(BufferedImage image, Map<String, Object> parameters) throws IOException {
        int width = (int) parameters.get("width");
        int height = (int) parameters.get("height");
        return Thumbnails.of(image).size(width, height).asBufferedImage();
    }
}