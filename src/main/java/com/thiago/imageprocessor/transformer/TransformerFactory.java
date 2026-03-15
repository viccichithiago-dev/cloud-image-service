package com.thiago.imageprocessor.transformer;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
@Component
public class TransformerFactory {
    private Map<String, ImageTransformer> transformers;

    public TransformerFactory() {
        transformers = new HashMap<>();
        transformers.put("RESIZE", new ResizeTransformer());
        transformers.put("CROP", new CropTransformer());
        transformers.put("ROTATE", new RotateTransformer());
        transformers.put("WATERMARK", new WatermarkTransformer());
        transformers.put("FLIP", new FlipTransformer());
        transformers.put("COMPRESS", new CompressTransformer());
        transformers.put("FORMAT_CHANGE", new FormatChangeTransformer());
        transformers.put("FILTER", new FilterTransformer());
    }

    public ImageTransformer getTransformer(String type) {
        return transformers.get(type);
    }
}