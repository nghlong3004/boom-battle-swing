package io.nghlong3004.boom_battle_swing.util;

import io.nghlong3004.boom_battle_swing.constant.MapConstant;
import io.nghlong3004.boom_battle_swing.model.BomberSkin;
import io.nghlong3004.boom_battle_swing.model.Tile;
import io.nghlong3004.boom_battle_swing.model.TileMode;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static io.nghlong3004.boom_battle_swing.constant.BomberConstant.BOMBER_LENGTH_HEIGHT;
import static io.nghlong3004.boom_battle_swing.constant.BomberConstant.BOMBER_LENGTH_WIDTH;
import static io.nghlong3004.boom_battle_swing.constant.ImageConstant.*;
import static io.nghlong3004.boom_battle_swing.model.Tile.M;
import static io.nghlong3004.boom_battle_swing.model.TileMode.N;

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
