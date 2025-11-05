package io.nghlong3004.manager;

import io.nghlong3004.assets.MapAssets;
import io.nghlong3004.constant.GameConstant;
import lombok.RequiredArgsConstructor;

import java.awt.*;

import static io.nghlong3004.type.TileType.FLOOR;

@RequiredArgsConstructor
public class GameRender {
    private final MapManager mapManager;
    private final BomberManager bomberManager;

    public void render(Graphics g) {
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
        bomberManager.render(g);
    }

}
