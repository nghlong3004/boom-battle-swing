package io.nghlong3004.boom_battle_swing.util;

import javax.imageio.ImageIO;
import java.awt.*;
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

    public static BufferedImage flipHorizontal(BufferedImage image) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage flipped = new BufferedImage(w, h, image.getType());
        Graphics2D g = flipped.createGraphics();
        g.drawImage(image, 0, 0, w, h, w, 0, 0, h, null);
        g.dispose();
        return flipped;
    }

    private ImageUtil() {
    }

}
