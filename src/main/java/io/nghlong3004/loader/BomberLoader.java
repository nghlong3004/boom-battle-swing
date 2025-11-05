package io.nghlong3004.loader;

import io.nghlong3004.type.SkinType;
import lombok.extern.slf4j.Slf4j;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static io.nghlong3004.constant.ImageConstant.*;

@Slf4j
public class BomberLoader {

    public static List<BufferedImage[][]> loadBomberAssets() {
        var images = new ArrayList<BufferedImage[][]>();
        for (var skin : SkinType.values()) {
            String name = BOMBER_SKIN_TEMPLATE.formatted(skin.getAssetKey());
            var image = loadBomberAsset(4, 5, IMAGE_BOMBER_WIDTH, IMAGE_BOMBER_HEIGHT, name);
            images.add(image);
        }
        return images;
    }

    public static BufferedImage[] loadBomberDeathAssets() {
        log.info("Loading bomber death animation sprites");
        BufferedImage deathSheet = ImageLoader.loadImage(BOMBER_DEAD);
        BufferedImage[] frames = new BufferedImage[BOMBER_DEAD_FRAMES];

        for (int i = 0; i < BOMBER_DEAD_FRAMES; i++) {
            int col = i % BOMBER_DEAD_COLS;
            frames[i] = deathSheet.getSubimage(col * BOMBER_DEAD_SPRITE_SIZE, 0, BOMBER_DEAD_SPRITE_SIZE,
                                               BOMBER_DEAD_SPRITE_SIZE);
        }

        log.info("Loaded {} bomber death animation frames", BOMBER_DEAD_FRAMES);
        return frames;
    }

    private static BufferedImage[][] loadBomberAsset(int n, int m, int width, int height, String name) {
        BufferedImage[][] animations = new BufferedImage[n][m];
        BufferedImage image = ImageLoader.loadImage(name);
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                animations[i][j] = image.getSubimage(j * width, i * height, width, height);
            }
        }
        return animations;
    }
}
