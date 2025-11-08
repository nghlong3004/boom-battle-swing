package io.nghlong3004.loader;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ImageLoader {
    
    private static final ConcurrentHashMap<String, BufferedImage> imageCache = new ConcurrentHashMap<>();

    public static BufferedImage loadImage(String name) {
        // Kiểm tra cache trước
        BufferedImage cached = imageCache.get(name);
        if (cached != null) {
            log.debug("Using cached image: {}", name);
            return cached;
        }
        
        // Nếu chưa có trong cache thì load
        log.info("Loading file name={}", name);
        try (InputStream inputStream = ImageLoader.class.getResourceAsStream(name)) {
            if (inputStream == null) {
                throw new RuntimeException("%s not found!".formatted(name));
            }
            BufferedImage image = ImageIO.read(inputStream);
            // Lưu vào cache
            imageCache.put(name, image);
            return image;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void clearCache() {
        imageCache.clear();
        log.info("Image cache cleared");
    }

    private ImageLoader() {
    }
}
