package io.nghlong3004.boom_battle_swing.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static io.nghlong3004.boom_battle_swing.constant.ImageConstant.LEVEL_ONE_DATA_LONG;

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

    public static BufferedImage[][] importImageBomber(int n, int m, String name, int width, int height) {
        BufferedImage[][] animations = new BufferedImage[n][m];
        BufferedImage image = ImageUtil.loadImage(name);
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                animations[i][j] = image.getSubimage(j * width, i * height, width, height);
            }
        }
        return animations;
    }

    public static int[][] importLevelData() {

        BufferedImage img = loadImage(LEVEL_ONE_DATA_LONG);
        int[][] lvlData = new int[img.getHeight()][img.getWidth()];

        for (int j = 0; j < img.getHeight(); j++) {
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getRed();
                if (value >= 48) {
                    value = 0;
                }
                lvlData[j][i] = value;
            }
        }
        return lvlData;

    }

    private ImageUtil() {
    }

}
