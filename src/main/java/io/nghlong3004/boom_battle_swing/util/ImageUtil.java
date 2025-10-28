package io.nghlong3004.boom_battle_swing.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public final class ImageUtil {

    public static BufferedImage loadImage(String name) {
        try (InputStream inputStream = ImageUtil.class.getResourceAsStream(name)) {
            if (inputStream == null) {
                throw new RuntimeException("Input Stream is null");
            }
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ImageUtil() {
    }

}
