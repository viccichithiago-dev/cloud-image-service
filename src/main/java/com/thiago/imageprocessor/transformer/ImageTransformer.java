package com.thiago.imageprocessor.transformer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

public interface ImageTransformer {
    BufferedImage transform(BufferedImage image, Map<String, Object> parameters) throws IOException;
}