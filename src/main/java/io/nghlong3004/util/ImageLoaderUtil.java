package io.nghlong3004.util;


import io.nghlong3004.constant.MapConstant;
import io.nghlong3004.model.BomberSkin;
import io.nghlong3004.model.Tile;
import io.nghlong3004.model.TileMode;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static io.nghlong3004.constant.BomberConstant.BOMBER_LENGTH_HEIGHT;
import static io.nghlong3004.constant.BomberConstant.BOMBER_LENGTH_WIDTH;
import static io.nghlong3004.constant.ImageConstant.*;
import static io.nghlong3004.model.Tile.M;
import static io.nghlong3004.model.TileMode.N;


@Slf4j
public final class ImageLoaderUtil {

    public static BufferedImage loadImage(String name) {
        log.info("Loading file name={}", name);
        try (InputStream inputStream = ImageLoaderUtil.class.getResourceAsStream(name)) {
            if (inputStream == null) {
                throw new RuntimeException("Input Stream is null");
            }
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage[][] loadImageBomber(int n, int m, int width, int height, String name) {
        BufferedImage[][] animations = new BufferedImage[n][m];
        BufferedImage image = ImageLoaderUtil.loadImage(name);
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                animations[i][j] = image.getSubimage(j * width, i * height, width, height);
            }
        }
        return animations;
    }

    public static List<BufferedImage[][]> loadBomberSkin() {
        var images = new ArrayList<BufferedImage[][]>();
        for (var skin : BomberSkin.values()) {
            String name = BOMBER_SKIN_TEMPLATE.formatted(skin.name);

            var image = ImageLoaderUtil.loadImageBomber(BOMBER_LENGTH_WIDTH, BOMBER_LENGTH_HEIGHT, IMAGE_BOMBER_WIDTH,
                                                        IMAGE_BOMBER_HEIGHT, name);
            images.add(image);
        }
        return images;
    }

    public static BufferedImage[] loadBombSprites() {
        log.info("Loading bomb sprites");
        BufferedImage[] bombSprites = new BufferedImage[8];
        for (int i = 0; i < 8; i++) {
            String path = BOMB_TEMPLATE.formatted(i + 1);
            bombSprites[i] = loadImage(path);
        }
        return bombSprites;
    }

    public static BufferedImage[] loadExplosionSprites() {
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

    public static BufferedImage[] loadExplosionAnimationFrames() {
        log.info("Loading explosion animation frames");
        BufferedImage explosionSheet = loadImage(EXPLOSION_ANIMATION);
        BufferedImage[] frames = new BufferedImage[EXPLOSION_FRAME_COUNT];

        for (int i = 0; i < EXPLOSION_FRAME_COUNT; i++) {
            frames[i] = explosionSheet.getSubimage(i * EXPLOSION_FRAME_WIDTH, 0, EXPLOSION_FRAME_WIDTH,
                                                   EXPLOSION_FRAME_HEIGHT);
        }

        return frames;
    }

    public static BufferedImage[] loadBomberDeathSprites() {
        log.info("Loading bomber death animation sprites");
        BufferedImage deathSheet = loadImage(BOMBER_DEAD);
        BufferedImage[] frames = new BufferedImage[BOMBER_DEAD_FRAMES];

        for (int i = 0; i < BOMBER_DEAD_FRAMES; i++) {
            int col = i % BOMBER_DEAD_COLS;
            frames[i] = deathSheet.getSubimage(col * BOMBER_DEAD_SPRITE_SIZE, 0, BOMBER_DEAD_SPRITE_SIZE,
                                               BOMBER_DEAD_SPRITE_SIZE);
        }

        log.info("Loaded {} bomber death animation frames", BOMBER_DEAD_FRAMES);
        return frames;
    }

    public static BufferedImage[][][] loadSoldierSprites() {
        log.info("Loading soldier sprites");
        BufferedImage[][][] soldierSprites = new BufferedImage[SOLDIER_TYPES][4][1];

        for (int type = 0; type < SOLDIER_TYPES; type++) {
            int folderIndex = type + 1;
            soldierSprites[type][0][0] = loadImage(SOLDIER_DOWN.formatted(folderIndex));
            soldierSprites[type][1][0] = loadImage(SOLDIER_LEFT.formatted(folderIndex));
            soldierSprites[type][2][0] = loadImage(SOLDIER_RIGHT.formatted(folderIndex));
            soldierSprites[type][3][0] = loadImage(SOLDIER_UP.formatted(folderIndex));
        }

        log.info("Loaded {} soldier types with 4 directions each", SOLDIER_TYPES);
        return soldierSprites;
    }

    public static BufferedImage[][] loadTileSprites() {
        log.info("Loading title sprites");
        var sprites = new BufferedImage[N.index][M.index];
        for (TileMode row : TileMode.values()) {
            if (row == N) {
                continue;
            }
            for (Tile column : Tile.values()) {
                if (column == M) {
                    continue;
                }
                loadTile(sprites, row, column);
            }
        }
        return sprites;
    }

    private static void loadTile(BufferedImage[][] sprites, TileMode row, Tile column) {
        String path = MapConstant.IMAGE_PATH_TEMPLATE.formatted(row.name, column.name);
        sprites[row.index][column.index] = loadImage(path);
    }

    private ImageLoaderUtil() {
    }

}
