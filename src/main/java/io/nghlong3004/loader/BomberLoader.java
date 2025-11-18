package io.nghlong3004.loader;

import io.nghlong3004.constant.ImageConstant;
import io.nghlong3004.model.type.MonsterType;
import io.nghlong3004.model.type.SkinType;
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
            var image = loadBomberAsset(name);
            images.add(image);
        }
        String name = BOMBER_SKIN_TEMPLATE.formatted(MonsterType.MONSTER.getAssetKey());
        var image = loadBomberAsset(name);
        images.add(image);
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

    private static BufferedImage[][] loadBomberAsset(String name) {
        BufferedImage[][] animations = new BufferedImage[4][5];
        BufferedImage image = ImageLoader.loadImage(name);
        for (int i = 0; i < 4; ++i) {
            for (int j = 0; j < 5; ++j) {
                animations[i][j] = image.getSubimage(j * ImageConstant.IMAGE_BOMBER_WIDTH,
                                                     i * ImageConstant.IMAGE_BOMBER_HEIGHT,
                                                     ImageConstant.IMAGE_BOMBER_WIDTH,
                                                     ImageConstant.IMAGE_BOMBER_HEIGHT);
            }
        }
        return animations;
    }

    private static BufferedImage[][] loadMonsterAsset(int id) {
        var animations = new BufferedImage[4][5];
        var directions = new String[]{"down", "left", "right", "up"};
        for (int i = 0; i < 4; ++i) {
            var image = ImageLoader.loadImage(MONSTER_TEMPLATE.formatted(id, directions[i]));
            int height = image.getHeight();
            int width = image.getWidth() >>> 1;
            for (int j = 0; j < 5; ++j) {
                animations[i][j] = image.getSubimage((j % 2) * width, 0, width, height);
            }
        }

        return animations;
    }
}
