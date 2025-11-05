package io.nghlong3004.manager;

import io.nghlong3004.assets.MapAssets;
import io.nghlong3004.constant.GameConstant;
import io.nghlong3004.loader.ImageLoader;
import lombok.RequiredArgsConstructor;

import java.awt.*;
import java.awt.image.BufferedImage;

import static io.nghlong3004.constant.ImageConstant.GAME_BACKGROUND;
import static io.nghlong3004.type.TileType.FLOOR;

@RequiredArgsConstructor
public class GameRender {
    private final MapManager mapManager;
    private final BomberManager bomberManager;
    private final BombManager bombManager;
    private BufferedImage background;

    public void render(Graphics g) {
        if (background == null) {
            background = ImageLoader.loadImage(GAME_BACKGROUND);
        }
        if (background != null) {
            g.drawImage(background, 0, 0, GameConstant.GAME_WIDTH, GameConstant.GAME_HEIGHT, null);
        }
        
        int row = mapManager.getMap()
                            .getType().id;
        var data = mapManager.getMap()
                             .getData();
        int spaceY = GameConstant.MAX_SCREEN_COLUMN - data[0].length >>> 1;
        int spaceX = GameConstant.MAX_SCREEN_ROW - data.length >>> 1;
        var images = MapAssets.getInstance()
                              .getMapAssets();
        for (int i = 0; i < data.length; ++i) {
            for (int j = 0; j < data[i].length; ++j) {
                int x = i + spaceX;
                int y = j + spaceY;
                g.drawImage(images[row][FLOOR.id], GameConstant.TILE_SIZE * y, GameConstant.TILE_SIZE * x,
                            GameConstant.TILE_SIZE, GameConstant.TILE_SIZE, null);
                int column = data[i][j];
                if (column != FLOOR.id && column < 4) {
                    g.drawImage(images[row][column], GameConstant.TILE_SIZE * y, GameConstant.TILE_SIZE * x,
                                GameConstant.TILE_SIZE, GameConstant.TILE_SIZE, null);
                }
            }
        }
        bombManager.render(g);
        bomberManager.render(g);
    }

}
