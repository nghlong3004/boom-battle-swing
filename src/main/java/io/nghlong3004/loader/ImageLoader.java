package io.nghlong3004.loader;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class ImageLoader {

    public static BufferedImage loadImage(String name) {
        log.info("Loading file name={}", name);
        try (InputStream inputStream = ImageLoader.class.getResourceAsStream(name)) {
            if (inputStream == null) {
                throw new RuntimeException("Input Stream is null");
            }
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ImageLoader() {
    }
}
