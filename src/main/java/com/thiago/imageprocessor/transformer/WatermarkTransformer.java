package com.thiago.imageprocessor.transformer;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

public class WatermarkTransformer implements ImageTransformer {
    @Override
    public BufferedImage transform(BufferedImage image, Map<String, Object> parameters) throws IOException {
        BufferedImage watermark = (BufferedImage) parameters.get("watermarkImage");
        float opacity = (float) parameters.getOrDefault("opacity", 0.5f);
        
        return Thumbnails.of(image)
                .scale(1.0)
                .watermark(Positions.BOTTOM_RIGHT, watermark, opacity)
                .asBufferedImage();
    }
}