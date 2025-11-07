package io.nghlong3004.loader;

import lombok.extern.slf4j.Slf4j;

import java.awt.image.BufferedImage;

import static io.nghlong3004.constant.ImageConstant.*;
import static io.nghlong3004.loader.ImageLoader.loadImage;

@Slf4j
public class ObjectLoader {

    public static BufferedImage[] loadBombAssets() {
        log.info("Loading custom bomb sprites");
        BufferedImage bombSheet = loadImage(CUSTOM_BOMB);
        BufferedImage[] bombSprites = new BufferedImage[CUSTOM_BOMB_FRAMES];
        
        if (bombSheet == null) {
            log.error("Failed to load custom bomb sprite sheet");
            return bombSprites;
        }
        
        for (int i = 0; i < CUSTOM_BOMB_FRAMES; i++) {
            bombSprites[i] = bombSheet.getSubimage(i * CUSTOM_BOMB_SPRITE_SIZE, 0, 
                                                    CUSTOM_BOMB_SPRITE_SIZE, CUSTOM_BOMB_SPRITE_SIZE);
        }
        
        log.info("Loaded {} custom bomb frames", CUSTOM_BOMB_FRAMES);
        return bombSprites;
    }

    public static BufferedImage[] loadExplosionAssets() {
        log.info("Loading explosion sprites");
        BufferedImage[] explosionSprites = new BufferedImage[9];
        explosionSprites[0] = loadImage(EXPLOSION_MID);
        explosionSprites[1] = loadImage(EXPLOSION_UP_1);
        explosionSprites[2] = loadImage(EXPLOSION_UP_2);
        explosionSprites[3] = loadImage(EXPLOSION_DOWN_1);
        explosionSprites[4] = loadImage(EXPLOSION_DOWN_2);
        explosionSprites[5] = loadImage(EXPLOSION_LEFT_1);
        explosionSprites[6] = loadImage(EXPLOSION_LEFT_2);
        explosionSprites[7] = loadImage(EXPLOSION_RIGHT_1);
        explosionSprites[8] = loadImage(EXPLOSION_RIGHT_2);
        return explosionSprites;
    }

    public static BufferedImage[] loadExplosionAnimationFrameAssets() {
        log.info("Loading explosion animation frames");
        BufferedImage explosionSheet = loadImage(EXPLOSION_ANIMATION);
        BufferedImage[] frames = new BufferedImage[EXPLOSION_FRAME_COUNT];

        for (int i = 0; i < EXPLOSION_FRAME_COUNT; i++) {
            frames[i] = explosionSheet.getSubimage(i * EXPLOSION_FRAME_WIDTH, 0, EXPLOSION_FRAME_WIDTH,
                                                   EXPLOSION_FRAME_HEIGHT);
        }

        return frames;
    }

    public static BufferedImage[] loadItemAssets() {
        log.info("Loading item images");
        BufferedImage[] items = new BufferedImage[3];
        items[0] = loadImage(ITEM_BOMB);
        items[1] = loadImage(ITEM_BOMB_SIZE);
        items[2] = loadImage(ITEM_SHOE);
        log.info("Loaded {} item images", items.length);
        return items;
    }

    public static BufferedImage[] loadItemEffectFramesAssets() {
        log.info("Loading item effect frames");
        BufferedImage[] effects = new BufferedImage[ITEM_EFFECT_FRAMES];
        for (int i = 0; i < ITEM_EFFECT_FRAMES; i++) {
            effects[i] = loadImage(ITEM_EFFECT_TEMPLATE.formatted(i + 1));
        }
        log.info("Loaded {} item effect frames", ITEM_EFFECT_FRAMES);
        return effects;
    }

    public static BufferedImage[] loadMoveEffectAssets() {
        log.info("Loading move effect frames");
        BufferedImage moveSheet = loadImage(MOVE_EFFECT);
        BufferedImage[] frames = new BufferedImage[MOVE_EFFECT_FRAMES];

        if (moveSheet == null) {
            log.error("Failed to load move effect sprite sheet");
            return frames;
        }

        for (int i = 0; i < MOVE_EFFECT_FRAMES; i++) {
            frames[i] = moveSheet.getSubimage(i * MOVE_EFFECT_FRAME_WIDTH, 0, 
                                              MOVE_EFFECT_FRAME_WIDTH, MOVE_EFFECT_FRAME_HEIGHT);
        }

        log.info("Loaded {} move effect frames", MOVE_EFFECT_FRAMES);
        return frames;
    }

    private ObjectLoader() {
    }
}
